package side.chatting.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import side.chatting.entity.Role;

@Data
@NoArgsConstructor
public class UserDto {

    private String username;
    private Role role;
    private String name;

    public UserDto(String username, Role role, String name) {
        this.username = username;
        this.role = role;
        this.name = name;
    }

    public UserDto(String username, String name) {
        this.username = username;
        this.name = name;
    }
}
