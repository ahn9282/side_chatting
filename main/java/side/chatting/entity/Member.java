package side.chatting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

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

    private String profile;
    private String description;

    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "member")
    private Set<UserChatRoom> chatRooms = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<Friend> friends = new HashSet<>();

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
