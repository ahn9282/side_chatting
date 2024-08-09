package side.chatting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import side.chatting.entity.Auth;
import side.chatting.entity.Grade;
import side.chatting.entity.Member;
import side.chatting.entity.Role;
import side.chatting.repository.AuthRepository;
import side.chatting.repository.MemberRepository;

import java.util.Scanner;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberApiControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AuthRepository authRepository;
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void mailCheck() throws Exception {
        String mail = "sdad@example.com";

        mockMvc.perform(get("/member/email/check")
                        .param("email", mail))
                .andExpect(status().isOk());
    }
    @Test
    void mailCheck2() throws Exception {
        String mail = "t1@ss.com";

        mockMvc.perform(get("/member/email/check")
                        .param("email", mail))
                .andExpect(status().isBadRequest());
    }
    @Test
    void mailCheck3() throws Exception {
        String mail = "dasasdsdaasdasd@naver.com";

        MockHttpServletResponse response = mockMvc.perform(get("/member/email/check")
                        .param("email", mail))
                .andReturn().getResponse();
        String contentAsString = response.getContentAsString();
        System.out.println("authNum = " + contentAsString);

    }

    @Test
    void mailDupleCheck() throws Exception {
        String mail = "userA@ex.com";

        MvcResult mvcResult = mockMvc.perform(get("/member/email/check")
                        .param("email", mail))
                .andExpect(status().isBadRequest())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        int status = response.getStatus();

        String errorMessage = response.getContentAsString();
        assertThat(errorMessage).isEqualTo("이미 가입된 이메일");
    }

    @Test
    void testFindId() throws Exception {
        Auth auth = authRepository.findByAuth(Role.USER);
        Member member = new Member("tester", "1234", "testerMan", Grade.SILVER, "junsyub@gmail.com");
        member.addAuth(auth);
        memberRepository.save(member);

        persistClear();

        String noExistEmail = "asdasd@asdas.com";
        String findAccountEmail = member.getEmail();

        MvcResult mvcResult1 = mockMvc.perform(post("/member/find/id")
                        .contentType("text/plain")
                        .content(noExistEmail))
                .andExpect(status().isBadRequest()).andReturn();

        String resp1 = mvcResult1.getResponse().getContentAsString();
        assertThat(resp1).isEqualTo("존재하지 않는 이메일입니다.");

        MvcResult mvcResult2 = mockMvc.perform(post("/member/find/id")
                        .contentType("text/plain")
                        .content(findAccountEmail))
                .andExpect(status().isOk())
                .andReturn();
        String resp2 = mvcResult2.getResponse().getContentAsString();
        assertThat(resp2).isEqualTo("success");
    }


    @Test
    void testFindPw() throws Exception {
        Auth auth = authRepository.findByAuth(Role.USER);
        Member member = new Member("tester", "1234", "testerMan", Grade.SILVER, "junsyub@gmail.com");
        member.addAuth(auth);
        memberRepository.save(member);

        persistClear();

        String noExistId = "asdfasdasfasf";
        String findAccount = member.getUsername();
        String findPwUri = "/member/find/pw";

        MvcResult mvcResult1 = mockMvc.perform(post(findPwUri)
                        .contentType("text/plain")
                        .content(noExistId))
                .andExpect(status().isBadRequest())
                .andReturn();
        String resp1 = mvcResult1.getResponse().getContentAsString();
        assertThat(resp1).isEqualTo("존재하지 않는 계정입니다.");

        MvcResult mvcResult2 = mockMvc.perform(post(findPwUri)
                        .contentType("text/plain")
                        .content(findAccount))
                .andExpect(status().isOk())
                .andReturn();
        String resp2 = mvcResult2.getResponse().getContentAsString();
        assertThat(resp2).isEqualTo("success");
        Member findMember = memberRepository.findByUsername("tester").get();
        log.info("before pw : {}, current Temp Password : {}", member.getPassword(), findMember.getPassword());

        assertThat(passwordEncoder.encode(member.getPassword())).isNotEqualTo(findMember.getPassword());
        assertThat(passwordEncoder.matches(member.getPassword(), findMember.getPassword())).isFalse();
    }



    private void persistClear(){
        entityManager.flush();
        entityManager.clear();
    }
}