package side.chatting.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginForm {
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

}
