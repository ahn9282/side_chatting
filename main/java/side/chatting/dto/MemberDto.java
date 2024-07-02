package side.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import side.chatting.entity.Auth;
import side.chatting.entity.Grade;
import side.chatting.entity.Member;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private String username;
    private String name;
    private Grade grade;
    private String password;

    public MemberDto(Member member) {
        this.username = member.getUsername();
        this.name = member.getName();
        this.grade = member.getGrade();
    }


}
