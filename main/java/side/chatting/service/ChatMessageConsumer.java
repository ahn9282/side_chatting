package side.chatting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.util.InternalException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import side.chatting.dto.Message;
import side.chatting.entity.ChatRoom;
import side.chatting.entity.Member;
import side.chatting.entity.MessageEntity;
import side.chatting.excpetion.NotExistChatRoomException;
import side.chatting.excpetion.NotFoundUserException;
import side.chatting.repository.ChatRoomRepository;
import side.chatting.repository.MemberRepository;
import side.chatting.repository.MessageRepository;

import java.util.Optional;


@Service
@Slf4j
public class ChatMessageConsumer {

    private final SimpMessagingTemplate messagingTemplate;
    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatMessageConsumer(SimpMessagingTemplate messagingTemplate, MemberRepository memberRepository,
                               MessageRepository messageRepository, ChatRoomRepository chatRoomRepository) {
        this.messagingTemplate = messagingTemplate;
        this.memberRepository = memberRepository;
        this.messageRepository = messageRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    @Transactional(readOnly = true)
    @KafkaListener(topics = "chatting", groupId = "my-group")
    public void listen(Message message, Acknowledgment acknowledgment) {
        log.info("컨슈머 동작 Record : {}", message);

        messagingTemplate.convertAndSend("/subscribe/someTopic", message);
        Optional<Member> sender = memberRepository.findById(message.getSenderId());
        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(message.getChatRoomId());
        if (sender.isPresent() && chatRoom.isPresent()) {
            MessageEntity msgData = new MessageEntity(message, sender.get(), chatRoom.get());
            msgSave(msgData);
            acknowledgment.acknowledge();
        }else if(sender.isPresent()){
            throw new NotFoundUserException("User Input Exception");
        }else if(chatRoom.isPresent()){
            throw new NotExistChatRoomException("Not Exist Room Exception");
        }else{
            throw new InternalException("Server Error");
        }

    }
    @Transactional
    private void msgSave(MessageEntity msgData){
        messageRepository.save(msgData);
    }
}