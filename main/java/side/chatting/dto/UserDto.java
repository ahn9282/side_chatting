package side.chatting.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private String username;
    private String role;
    private String name;

    public UserDto(String username, String role, String name) {
        this.username = username;
        this.role = role;
        this.name = name;
    }

    public UserDto(String username, String name) {
        this.username = username;
        this.name = name;
    }
}
