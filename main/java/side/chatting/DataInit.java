package side.chatting;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import side.chatting.entity.Auth;
import side.chatting.entity.Member;
import side.chatting.entity.Role;
import side.chatting.repository.AuthRepository;
import side.chatting.repository.MemberRepository;

//@Component
@Transactional
@RequiredArgsConstructor
public class DataInit {

    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;


    @PostConstruct
    public void DataInit() {
        init();
    }

    private void init(){
        memberRepository.deleteAll();
        authRepository.deleteAll();
        Auth auth = new Auth(Role.ADMIN);
        Auth auth2 = new Auth(Role.USER);
        authRepository.save(auth);
        authRepository.save(auth2);

        Member member1 = new Member("test1", passwordEncoder.encode("1234"), "userA", auth);
        Member member2 = new Member("test2", passwordEncoder.encode("1234"), "userB", auth);
        Member member3 = new Member("test3", passwordEncoder.encode("1234"), "userC", auth2);
        Member member4 = new Member("test4", passwordEncoder.encode("1234"), "userD", auth2);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
    }
}
