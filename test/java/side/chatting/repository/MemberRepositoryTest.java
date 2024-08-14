package side.chatting.repository;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import side.chatting.dto.ChatPageDto;
import side.chatting.entity.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthRepository authRepository;


    @Test
    void joinTest(){
        Member member = new Member("memberTest", passwordEncoder.encode( "1234"), "name1");
        memberRepository.save(member);
        em.flush();
        em.clear();

        Member findMember = memberRepository.findByUsername("memberTest").get();

        log.info("member's pw : {}", findMember.getPassword());
        assertThat(findMember.getUsername()).isEqualTo("memberTest");
        assertThat(passwordEncoder.matches("1234", findMember.getPassword())).isTrue();
        assertThat(findMember.getName()).isEqualTo("name1");
    }
    @Test
    void memberDuplicateTest(){
        Member member1 = new Member("test1", passwordEncoder.encode( "1234"), "name1");
        Member member2 = new Member("memberTest", passwordEncoder.encode( "1234"), "userA");
        assertThrows(DataIntegrityViolationException.class, () -> memberRepository.save(member1));//id중복
        assertThrows(DataIntegrityViolationException.class, () -> memberRepository.save(member2));//닉넴 중복
    }

    @Test
    void addedDataTest(){
        Auth auth = authRepository.findByAuth(Role.USER);
        Member member1 = new Member("testMem", passwordEncoder.encode("1234"), "name1", auth);
        memberRepository.save(member1);

        em.flush();
        em.clear();

        System.out.println("===============================================");
        Member findMember = memberRepository.findByUsername("testMem").get();
        assertThat(findMember.getAuth().getAuth()).isEqualTo(auth.getAuth());
        assertThat(findMember.getCreatedDate()).isNotNull();
        assertThat(findMember.getUpdatedDate()).isNotNull();
        assertThat(findMember.getUpdatedDate().isEqual(findMember.getCreatedDate())).isTrue();
    }

    @Test
    void testGetMemberWithAuth(){

        Member result1 = memberRepository.findMemberWithAuth("test1").get();


        assertThat(result1.getUsername()).isEqualTo("test1");
        assertThat(String.valueOf(result1.getAuth().getAuth())).isEqualTo("ROLE_USER");

    }

    @Test
    void mainPageDataTest() {
        List<Member> results = memberRepository.withFriends(1L);
        log.info("result : {}", results);
    }
    @Test
    void mainPageDataTest2() {
        String username = "test1";
        Optional<Member> findMember = memberRepository.findByUsername(username);
        assertThat(findMember.isPresent()).isTrue();
        assertThat(findMember.isEmpty()).isFalse();
        Member member = findMember.get();
        Set<ChatMember> chatMemberSet = member.getChatMemberSet();
        Set<FriendShip> friendsAsMember = member.getFriendsAsFriend();
        log.info("chatRoomMember: {}", chatMemberSet);
        log.info("friends : {}", friendsAsMember);

    }

    @Test
    void mainPageDataTest3() {


        List<Member> result = memberRepository.withFriends(1L);
        log.info("result test : {}", result);
    }

}