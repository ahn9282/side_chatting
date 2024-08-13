package side.chatting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Entity
@Getter
@Setter
public class Member extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;

    private String password;


    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="auth_id")
    private Auth auth;

    @Enumerated(EnumType.STRING)
    private Grade grade;


    private String profile;
    private String description;

    private String email;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageEntity> messages = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatMember> chatMemberSet = new HashSet<>();


    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FriendShip> friendsAsMember = new HashSet<>();

    @OneToMany(mappedBy = "friend", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FriendShip> friendsAsFriend = new HashSet<>();

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

    public Member(String username, String password, String name, Grade grade, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.grade = grade;
        this.email = email;
    }

    public Member(String username, String name) {
        this.username = username;
        this.name = name;
    }

    public void addAuth(Auth auth) {
        this.setAuth(auth);
        auth.getMembers().add(this);
    }


    public void log() {
        log.info("Member [ username : {}, name : {}, email : {}]", username, name, email);
    }
    @Override
    public String toString(){
        return "Member >> ID : " + this.username + ", name : " + this.name;
    }
}
