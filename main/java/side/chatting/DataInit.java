package side.chatting;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import side.chatting.entity.Auth;
import side.chatting.entity.Member;
import side.chatting.entity.Role;
import side.chatting.repository.AuthRepository;
import side.chatting.repository.MemberRepository;

import static side.chatting.entity.Role.*;

@Component
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
        Auth auth = new Auth(ROLE_USER);
        authRepository.save(auth);

        Member member1 = new Member("test1", passwordEncoder.encode("1234"), "userA", auth);
        Member member2 = new Member("test2", passwordEncoder.encode("1234"), "userB", auth);
        Member member3 = new Member("test3", passwordEncoder.encode("1234"), "userC", auth);
        Member member4 = new Member("test4", passwordEncoder.encode("1234"), "userD", auth);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
    }
}
