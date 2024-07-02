package side.chatting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_id")
    private Auth auth;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    public Member() {

    }

    public Member(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public Member(String username, String password, String name, Auth auth) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.auth = auth;
    }

    public Member(String username, String password, String name, Grade grade) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.grade = grade;
    }

    public void addAuth(Auth auth) {
        this.setAuth(auth);
        auth.getMembers().add(this);
    }
}
