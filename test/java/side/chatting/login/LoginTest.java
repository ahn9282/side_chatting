package side.chatting.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import side.chatting.dto.LoginForm;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void loginTest() throws Exception {
        String username = "test1";
        String password = "1234";

        String loginForm = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(loginForm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists("access"))
                .andExpect(MockMvcResultMatchers.cookie().exists("refresh"));
    }
    @Test
    void test2() throws Exception {
        String username = "test123";
        String password = "1234";
        LoginForm form = new LoginForm(username, password);
        String loginForm = objectMapper.writeValueAsString(form);
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(loginForm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }
}
