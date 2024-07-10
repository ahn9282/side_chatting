package side.chatting.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import side.chatting.entity.Message;
import side.chatting.repository.ChatRoomRepository;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations messageSendingTemplate;
    private final ChatRoomRepository chatRoomRepository;

    @MessageMapping("chat/message")
    public void message(Message message) {

        messageSendingTemplate.convertAndSend("/sub/chat/room/" + message.getChatRoom().getId(), message);
    }
}
