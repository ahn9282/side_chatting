package side.chatting.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import side.chatting.dto.Message;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatMessageConsumer {
    private final SimpMessagingTemplate messagingTemplate;




    @KafkaListener(topics = "chatting", groupId = "my-group")
    public void listen(Message message, String topic){
        Long chatRoomId = message.getChatRoomId();
        messagingTemplate.convertAndSend("/subscribe/chatRoom/" + chatRoomId, message);
        log.info("Message received from chat-room-{}: {}", chatRoomId, message);
    }

}
