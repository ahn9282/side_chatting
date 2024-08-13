package side.chatting.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import side.chatting.entity.ChatRoom;
import side.chatting.repository.ChatRoomRepository;

@Data
public class ChatRoomDto {

    private Long chatRoomId;

    public ChatRoomDto() {
    }

    public ChatRoomDto(ChatRoom chatRoom) {
        this.chatRoomId = chatRoom.getId();

    }
}
