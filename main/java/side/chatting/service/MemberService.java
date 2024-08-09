package side.chatting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import side.chatting.controller.MemberApiController;
import side.chatting.dto.JoinForm;
import side.chatting.dto.MemberDto;
import side.chatting.entity.Member;
import side.chatting.repository.AuthRepository;
import side.chatting.repository.MemberRepository;

import java.util.Optional;


public interface MemberService {


    MemberDto joinMember(JoinForm form);

    Boolean checkDupleUsername(String id);

    Boolean checkDupleName(String name);

    Optional<Member> checkDupleEmail(String email);

    String findPassWordProcess(String username);


}
