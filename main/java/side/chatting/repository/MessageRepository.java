package side.chatting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import side.chatting.entity.MessageEntity;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {


}
