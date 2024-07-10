package side.chatting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import side.chatting.dto.MemberDto;
import side.chatting.repository.AuthRepository;
import side.chatting.repository.MemberRepository;


public interface MemberService {


    MemberDto joinMember(MemberDto form);

    Boolean checkDupleUsername(MemberDto form);

    Boolean checkDupleName(MemberDto form);

    Boolean checkDupleEmail(String email);
}
