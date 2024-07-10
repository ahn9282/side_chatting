package side.chatting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import side.chatting.dto.CustomUser;
import side.chatting.entity.Member;
import side.chatting.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member userData = memberRepository.findMemberWithAuth(username);
        if (userData != null) {
            return new CustomUser(userData, false);
        }
        throw new UsernameNotFoundException("아이디가 존재하지 않습니다.");
    }
}
