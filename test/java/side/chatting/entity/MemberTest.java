package side.chatting.entity;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import side.chatting.repository.MemberRepository;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional(readOnly = true)
public class MemberTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Transactional
    @Test
    void memberFriendTest(){
        Member test3 = memberRepository.findByUsername("test3").get();
        Member test2 = memberRepository.findByUsername("test2").get();

        test3.addFriend(test2);
        em.flush();
        em.clear();

        Member findMember2 = memberRepository.findByUsername("test2").get();
        Set<Member> friends = findMember2.getFriends();
        assertThat(friends.size()).isEqualTo(1);


    }
}
