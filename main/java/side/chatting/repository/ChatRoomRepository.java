package side.chatting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import side.chatting.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
