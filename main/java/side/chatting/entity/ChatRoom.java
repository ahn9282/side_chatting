package side.chatting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter@Setter
@Entity
public class ChatRoom extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topicName;

    @ManyToMany(mappedBy = "chatRooms")
    private Set<Member> members;

}