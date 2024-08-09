package side.chatting.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class JoinForm {

    private String username;
    private String password;
    private String email;
    private String name;

    public JoinForm(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
    }
}
