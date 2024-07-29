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

    @ManyToMany
    @JoinTable(
            name = "member_chatroom",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "chatroom_id")
    )
    private Set<ChatRoom> chatRooms;


    @ManyToMany
    @JoinTable(name = "friendship",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private Set<Member> friends = new HashSet<>();

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

    public Member(String username, String name) {
        this.username = username;
        this.name = name;
    }

    public void addAuth(Auth auth) {
        this.setAuth(auth);
        auth.getMembers().add(this);
    }

    public void addFriend(Member friend) {
        if (!this.friends.contains(friend)) {
            this.friends.add(friend);
        }
        if (!friend.getFriends().contains(this)) {
            friend.getFriends().add(this);
        }
    }

    public void removeFriend(Member friend) {
        if (this.friends.contains(friend)) {
            this.friends.remove(friend);
        }
        if (friend.getFriends().contains(this)) {
            friend.getFriends().remove(this);
        }
    }
    public void createChatroomForMember(Member member, ChatRoom chatroom) {
        member.getChatRooms().add(chatroom);
        chatroom.getMembers().add(member);
    }}
