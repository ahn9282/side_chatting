package side.chatting.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import side.chatting.dto.JoinForm;
import side.chatting.dto.MemberDto;
import side.chatting.entity.Member;
import side.chatting.repository.MemberRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
class MemberServiceImplTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @PersistenceContext
    EntityManager em;
    @Test
    void validationServiceTest(){
            MemberDto data = new MemberDto();
            data.setUsername("test1");
            data.setName("userA");
        assertThat(memberService.checkDupleName(data.getName())).isFalse();
        assertThat(memberService.checkDupleUsername(data.getUsername())).isFalse();
        data.setUsername("test123");
        data.setName("userAqwe");
        assertThat(memberService.checkDupleName(data.getName())).isTrue();
        assertThat(memberService.checkDupleUsername(data.getUsername())).isTrue();
    }

    @Test
    void JoinMemberTest() throws InterruptedException {
        JoinForm form = new JoinForm("jointest", "1234", "jointester", "asd@asd.com");
        memberService.joinMember(form);
        em.flush();
        em.clear();

        Optional<Member> findUser = memberRepository.findByUsername(form.getUsername());
        Map<String, Boolean> checkerMap = memberChecker(findUser.get(), form);
        findUser.get().log();
        assertThat(form.getEmail()).isNotNull();
        assertThat(checkerMap.isEmpty()).isTrue();

    }
    @Test
    void getMember(){
        Optional<Member> findMember = memberRepository.findByUsername("test1");
        findMember.get().log();
        assertThat(findMember.get().getEmail()).isNotNull();
    }

    private Map<String, Boolean> memberChecker(Member member,JoinForm form) {
        Map<String, Boolean> map = new HashMap<>();
        if( ! member.getUsername().equals(form.getUsername())){
            map.put("username", false);
        }
        if( ! member.getPassword().equals(form.getPassword())){
            map.put("getPassword", false);

        }
        if( ! member.getName().equals(form.getName())){

            map.put("name", false);
        }
        if( ! member.getEmail().equals(form.getEmail())){
            map.put("email", false);

        }
        return map;
    }

}