package side.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Message {
    private LocalDateTime createdDate;
    private Long chatRoomId;
    private String content;
    private Long senderId;
    private long readCount;

    public Message() {
        this.readCount = 1;

    }
}
