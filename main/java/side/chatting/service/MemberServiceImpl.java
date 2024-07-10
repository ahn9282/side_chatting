package side.chatting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import side.chatting.dto.MemberDto;
import side.chatting.entity.Auth;
import side.chatting.entity.Grade;
import side.chatting.entity.Member;
import side.chatting.repository.AuthRepository;
import side.chatting.repository.MemberRepository;

import java.util.Optional;


@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;


    @Transactional
    @Override
    public MemberDto joinMember(MemberDto form) {
        Auth auth = authRepository.findByAuth("ROLE_USER");
        Member newMember = new Member(form.getUsername(), form.getPassword(), form.getName(), Grade.SILVER);
        newMember.addAuth(auth);
        Member saveMember = memberRepository.save(newMember);
        return new MemberDto(saveMember);
    }

    @Override
    public Boolean checkDupleUsername(MemberDto form) {
        Optional<Member> dupleUsernameMember = memberRepository.findByUsername(form.getUsername());
        return dupleUsernameMember.isPresent();
    }

    @Override
    public Boolean checkDupleName(MemberDto form) {
        Optional<Member> dupleUsernameMember = memberRepository.findByName(form.getName());
        return dupleUsernameMember.isPresent();
    }

    @Override
    public Boolean checkDupleEmail(String email) {
        Optional<Member> dupleEmailMember = memberRepository.findByEmail(email);
        return dupleEmailMember.isPresent();
    }

}
