package side.chatting.repository;

import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import side.chatting.entity.Auth;
import side.chatting.entity.Member;
import side.chatting.entity.Role;

import java.util.List;

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

        Member findMember = memberRepository.findByUsername("memberTest");

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
        Auth auth = authRepository.findByAuth(Role.ROLE_USER);
        Member member1 = new Member("testMem", passwordEncoder.encode("1234"), "name1", auth);
        memberRepository.save(member1);

        em.flush();
        em.clear();

        System.out.println("===============================================");
        Member findMember = memberRepository.findByUsername("testMem");
        assertThat(findMember.getAuth().getAuth()).isEqualTo(auth.getAuth());
        assertThat(findMember.getCreatedDate()).isNotNull();
        assertThat(findMember.getUpdatedDate()).isNotNull();
        assertThat(findMember.getUpdatedDate().isEqual(findMember.getCreatedDate())).isTrue();
    }

    @Test
    void testGetMemberWithAuth(){

        Member result1 = memberRepository.findMemberWithAuth("test1");


        assertThat(result1.getUsername()).isEqualTo("test1");
        assertThat(String.valueOf(result1.getAuth().getAuth())).isEqualTo("ROLE_USER");

    }


}