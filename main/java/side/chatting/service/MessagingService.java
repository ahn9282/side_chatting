package side.chatting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import side.chatting.repository.ChatRoomRepository;

@Service
public class MessagingService {

    private KafkaTemplate<String, String> kafkaTemplate;
    private final ChatRoomRepository chatRoomRepository;

    public MessagingService(ChatRoomRepository chatRoomRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.chatRoomRepository = chatRoomRepository;
        this.kafkaTemplate = kafkaTemplate;
    }


}
