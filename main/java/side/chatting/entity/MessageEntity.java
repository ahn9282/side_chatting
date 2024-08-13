package side.chatting.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import side.chatting.dto.Message;

@Entity
@Getter
@Setter
@Table(name="message")
@NoArgsConstructor
public class MessageEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Lob
    private String content;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;
    private long readCount;

    public MessageEntity(Message message, Member member, ChatRoom chatRoom) {
        this.content = message.getContent();
        this.member = member;
        member.getMessages().add(this);
        this.chatRoom = chatRoom;
        chatRoom.getMessages().add(this);

    }
}
