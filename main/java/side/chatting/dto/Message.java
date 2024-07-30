package side.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private Long id;
    private LocalDateTime createdDate;
    private Long chatRoomId;
    private String content;
    private Long senderId;
    private long readCount;

}
