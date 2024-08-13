package side.chatting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import side.chatting.entity.ChatRoom;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {


}
