package side.chatting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberApiControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    void mailCheck() throws Exception {
        String mail = "sdad@example.com";

        mockMvc.perform(MockMvcRequestBuilders.get("/member/email/check")
                        .param("email", mail))
                .andExpect(status().isOk());
    }
    @Test
    void mailCheck2() throws Exception {
        String mail = "t1@ss.com";

        mockMvc.perform(MockMvcRequestBuilders.get("/member/email/check")
                        .param("email", mail))
                .andExpect(status().isBadRequest());
    }
    @Test
    void mailCheck3() throws Exception {
        String mail = "dasasdsdaasdasd@naver.com";

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/member/email/check")
                        .param("email", mail))
                .andReturn().getResponse();
        String contentAsString = response.getContentAsString();
        System.out.println("authNum = " + contentAsString);

    }
}