package side.chatting.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.CloseStatus;
import side.chatting.dto.Message;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
 * WebSocket Handler 작성
 * 소켓 통신은 서버와 클라이언트가 1:n으로 관계를 맺는다. 따라서 한 서버에 여러 클라이언트 접속 가능
 * 서버에는 여러 클라이언트가 발송한 메세지를 받아 처리해줄 핸들러가 필요
 * TextWebSocketHandler를 상속함, 클라이언트로 받은 메세지를 처리 후 클라이언트로 환영 메세지를 보내줌
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class MultipleWebSocketHandler extends TextWebSocketHandler {

    private final KafkaTemplate<String, Message> kafkaTemplate;
    private final ObjectMapper objectMapper;
    // 현재 연결된 세션들
    private final Set<WebSocketSession> sessions = new HashSet<>();

    // chatRoomId: {session1, session2}
    private final Map<Long, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 클라이언트와의 연결이 설정되었을 때 호출
        sessions.add(session);
        log.info("연결 Client  id : {}", session.getId());
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 클라이언트와의 연결이 종료되었을 때 호출
        String clientId = session.getId();
        log.info("client close : {}", clientId);
        sessions.remove(session);
    }

    // 객체 메시지를 처리할 때 호출
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // 커스텀
        log.info("메시지 받음 : {}", message.getPayload());
        Message messageObject = objectMapper.readValue((String)message.getPayload(), Message.class);
        String topic = "chat-room-" + messageObject.getChatRoomId();
        kafkaTemplate.send(topic, messageObject);
        log.info("토픽 {}로 메시지 전송 내용 : {}", topic, messageObject.getContent());
    }
}
