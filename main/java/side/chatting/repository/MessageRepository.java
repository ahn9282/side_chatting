package side.chatting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import side.chatting.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
