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
        Member dupleUsernameMember = memberRepository.findByUsername(form.getUsername());
        if(dupleUsernameMember != null){
            return false;
        }
        return true;
    }

    @Override
    public Boolean checkDupleName(MemberDto form) {
        Member dupleUsernameMember = memberRepository.findByName(form.getName());
        if(dupleUsernameMember != null){
            return false;
        }
        return true;
    }

}
