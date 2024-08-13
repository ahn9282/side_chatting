package side.chatting.controller;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import side.chatting.dto.ChatPageDto;
import side.chatting.entity.Member;
import side.chatting.jwt.JwtUtil;
import side.chatting.repository.MemberRepository;

@Slf4j
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class ChatApiControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    MemberRepository memberRepository;
    @PersistenceContext
    EntityManager em;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void authOkP() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"test1\", \"password\":\"1234\"}"))
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String header = response.getHeader("access");
        log.info("token : {}", header);
        Member member = memberRepository.findByUsername("test1").get();
        em.flush();
        em.clear();
        Assertions.assertThat(jwtUtil.getUsername(header)).isEqualTo(member.getUsername());
        Assertions.assertThat(jwtUtil.getName(header)).isEqualTo(member.getName());

        MvcResult mvcResult1 = mockMvc.perform(get("/user/chat")
                        .param("pageNum", "1")
                        .header("access", header))
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response1 = mvcResult1.getResponse();
        String contentAsString = response1.getContentAsString();
        log.info("final Result : {}", contentAsString);
       // ChatPageDto chatPageDto = objectMapper.readValue(contentAsString, ChatPageDto.class);
        //log.info("result : {}", chatPageDto);
    }


}