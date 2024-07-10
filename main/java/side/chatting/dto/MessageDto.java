package side.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import side.chatting.entity.MessageStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Long id;
    private LocalDateTime createdDate;
    private Long chatRoomId;
    private String content;
    private Long memberId;
    private MessageStatus status;

}
