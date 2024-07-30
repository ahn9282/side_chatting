package side.chatting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import side.chatting.dto.Message;

import java.lang.reflect.Type;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
@RequiredArgsConstructor
public class WebSocketKafkaIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testWebSocketAndKafka() throws Exception {
        String websocketUrl = "ws://localhost:9282/chat-stomp";
        String testMessageContent = "Hello Kafka!";
        Long testChatRoomId = 1L;

        // CountDownLatch to wait for the message
        CountDownLatch latch = new CountDownLatch(1);

        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        StompSession session = stompClient.connect(websocketUrl, new StompSessionHandlerAdapter() {}).get(5, TimeUnit.SECONDS);

        // Subscribe to the topic
        session.subscribe("/subscribe/chatRoom/" + testChatRoomId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Message.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Message receivedMessage = (Message) payload;
                assertEquals(testMessageContent, receivedMessage.getContent());
                latch.countDown();
            }
        });

        // Send a test message
        Message testMessage = new Message();
        testMessage.setChatRoomId(testChatRoomId);
        testMessage.setContent(testMessageContent);

        String payload = objectMapper.writeValueAsString(testMessage);
        session.send("/publish/sendMessage", payload);

        // Wait for the message to be received
        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new AssertionError("Message not received within timeout");
        }
    }
}
