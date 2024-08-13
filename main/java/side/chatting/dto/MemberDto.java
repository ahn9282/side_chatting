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
    private Long id;
    private String username;
    private String name;
    private Grade grade;
    private String profile;
    private String description;
    private String email;

    public MemberDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.name = member.getName();
        this.grade = member.getGrade();
    }



}
