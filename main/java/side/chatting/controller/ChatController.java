package side.chatting.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import side.chatting.dto.ChatPageDto;
import side.chatting.dto.CustomUser;
import side.chatting.dto.Message;
import side.chatting.entity.Member;
import side.chatting.jwt.JwtUtil;
import side.chatting.repository.ChatRoomRepository;
import side.chatting.repository.MemberRepository;
import side.chatting.repository.MemberRepositoryCustom;
import side.chatting.service.ChatMessageProducer;

import java.util.Optional;

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
