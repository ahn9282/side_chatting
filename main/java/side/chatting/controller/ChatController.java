package side.chatting.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import side.chatting.dto.Message;
import side.chatting.repository.ChatRoomRepository;
import side.chatting.service.ChatMessageProducer;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageProducer chatMessageProducer;

    @MessageMapping("/sendMessage")
    public void handleMessage(Message message) {
        log.info("messaging : {}", message);
        Long chatRoomId = message.getChatRoomId();
        String topic = "chat-room-" + chatRoomId;
        chatMessageProducer.sendMessage( message);
        log.info("Message received in controller and sent to Kafka topic: {} msg: {}", topic, message);
    }



}
