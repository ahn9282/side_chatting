package side.chatting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import side.chatting.dto.JoinForm;
import side.chatting.dto.MemberDto;
import side.chatting.entity.Auth;
import side.chatting.entity.Grade;
import side.chatting.entity.Member;
import side.chatting.entity.Role;
import side.chatting.repository.AuthRepository;
import side.chatting.repository.MemberRepository;

import java.util.Optional;
import java.util.UUID;


@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    @Override
    public MemberDto joinMember(JoinForm form) {
        Auth auth = authRepository.findByAuth(Role.USER);
        Member newMember = new Member(form.getUsername(), passwordEncoder.encode(form.getPassword()), form.getName(), Grade.SILVER, form.getEmail());
        newMember.addAuth(auth);
        Member saveMember = memberRepository.save(newMember);
        return new MemberDto(saveMember);
    }

    @Override
    public Boolean checkDupleUsername(String id) {
        Optional<Member> dupleUsernameMember = memberRepository.findByUsername(id);
        return dupleUsernameMember.isPresent();
    }

    @Override
    public Boolean checkDupleName(String name) {
        Optional<Member> dupleUsernameMember = memberRepository.findByName(name);
        return dupleUsernameMember.isPresent();
    }

    @Override
    public Optional<Member> checkDupleEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional
    @Override
    public String findPassWordProcess(String username) {
        Optional<Member> findMember = memberRepository.findByUsername(username);
        if(findMember.isEmpty()){
            return null;
        }
        String tempPassWord =  UUID.randomUUID().toString().substring(0, 9);
        findMember.get().setPassword(passwordEncoder.encode(tempPassWord));
        String email = findMember.get().getEmail();

        return email +"&" +tempPassWord;
    }

}
