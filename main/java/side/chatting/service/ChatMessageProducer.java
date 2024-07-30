package side.chatting.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import side.chatting.dto.Message;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageProducer {

    private final KafkaTemplate<String, Message> kafkaTemplate;

    public void sendMessage( Message message) {
        kafkaTemplate.send("chatting", message);
        log.info("Message send to Chatroom  msg : {}", message);
    }
}
