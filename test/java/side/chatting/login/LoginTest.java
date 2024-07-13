package side.chatting.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import side.chatting.dto.LoginForm;
import side.chatting.jwt.JwtUtil;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    JwtUtil jwtUtil;

    @Test
    public void testNoJwtTokenAccessUser() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isForbidden());
    }

    @Test
    void testJwtTokenAccessUser() throws Exception {
        String username = "test1";
        String password = "1234";

        String loginForm = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

        MockHttpServletResponse response = mockMvc.perform(post("/login")
                        .content(loginForm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists("access"))
                .andExpect(MockMvcResultMatchers.cookie().exists("refresh"))
                .andReturn()
                .getResponse();

        String accessToken = response.getHeader("access");
        String refreshToken = response.getCookie("refresh").getValue();
        String role = jwtUtil.getRole(accessToken);
        assertThat(role).isEqualTo("ROLE_USER");
        mockMvc.perform(get("/user")
                        .header("access", accessToken))
                .andExpect(status().isOk());
    }

    @Test
    void testLogoutAccess() throws Exception {
        String username = "test1";
        String password = "1234";

        String loginForm = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

        MockHttpServletResponse response = mockMvc.perform(post("/login")
                        .content(loginForm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists("access"))
                .andExpect(MockMvcResultMatchers.cookie().exists("refresh"))
                .andReturn()
                .getResponse();

        String access1 = response.getHeader("access");
        String header1 = response.getCookie("refresh").getValue();

        MockHttpServletResponse responseLogout = mockMvc.perform(post("/logout")
                        .header("access",access1)
                        .cookie(new Cookie("refresh", header1)))
                .andExpect(status().isOk())
                .andReturn().getResponse();



        String access2 = responseLogout.getHeader("access");
        assertThat(access2).isNullOrEmpty();
        mockMvc.perform(get("/user"))
                .andExpect(status().isForbidden());
    }
    @Test
    void loginTest() throws Exception {
        String username = "test1";
        String password = "1234";

        String loginForm = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

        mockMvc.perform(post("/login")
                        .content(loginForm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists("access"))
                .andExpect(MockMvcResultMatchers.cookie().exists("refresh"));
    }
    @Test
    void testWrongUsername() throws Exception {
        String username = "test123";
        String password = "1234";
        LoginForm form = new LoginForm(username, password);
        String loginForm = objectMapper.writeValueAsString(form);
        mockMvc.perform(post("/login")
                        .content(loginForm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }
    @Test
    void testWrongPassword() throws Exception {
        String username = "test1";
        String password = "123442141";

        LoginForm form = new LoginForm(username, password);
        String loginForm = objectMapper.writeValueAsString(form);
        mockMvc.perform(post("/login")
                        .content(loginForm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(result -> {
                   String errorMessage = result.getResponse().getContentAsString();
                    System.out.println("errorMessage = " + errorMessage);
                });

    }
    @Test
    void testJwtContent() throws Exception {
        String username = "test1";
        String password = "1234";
        LoginForm form = new LoginForm(username, password);
        String loginForm = objectMapper.writeValueAsString(form);
        MockHttpServletResponse response = mockMvc.perform(post("/login")
                        .content(loginForm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        String access = response.getHeader("access");
        assertThat(jwtUtil.getUsername(access)).isEqualTo("test1");
        assertThat(jwtUtil.getRole(access)).isEqualTo("ROLE_USER");
        assertThat(jwtUtil.getName(access)).isEqualTo("userA");


    }

    @Test
    void testCheckHeaderAccess() throws Exception {
        String username = "test1";
        String password = "1234";
        LoginForm form = new LoginForm(username, password);
        String loginForm = objectMapper.writeValueAsString(form);
        MockHttpServletResponse response = mockMvc.perform(post("/login")
                        .content(loginForm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        String access = response.getHeader(jwtUtil.HEADER_STRING);
        assertThat(jwtUtil.getUsername(access)).isEqualTo("test1");
        assertThat(access).isNotNull();
    }


}
