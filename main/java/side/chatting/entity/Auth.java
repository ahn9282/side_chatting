package side.chatting.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="authority")
public class Auth extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="auth_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, unique=true)
    private Role auth;

    @OneToMany(mappedBy = "auth", cascade=CascadeType.ALL)
    private List<Member> members = new ArrayList<>();

    public Auth() {
    }

    public Auth(Role role) {
        this.auth = role;
    }
}
