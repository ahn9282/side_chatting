package side.chatting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Friend {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

}
