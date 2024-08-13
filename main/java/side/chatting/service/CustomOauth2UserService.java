package side.chatting.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import side.chatting.dto.CustomUser;
import side.chatting.dto.NaverResponse;
import side.chatting.dto.Oauth2Response;
import side.chatting.dto.UserDto;
import side.chatting.entity.Auth;
import side.chatting.entity.Member;
import side.chatting.entity.Role;
import side.chatting.repository.AuthRepository;
import side.chatting.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2UserService extends DefaultOAuth2UserService  {

    private final MemberRepository memberRepository;
    private final AuthRepository authRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException  {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Oauth2Response oauth2Response = null;
        if (registrationId.equals("naver")) {
            oauth2Response = new NaverResponse(oauth2User.getAttributes());
        }
        String username = oauth2Response.getProvider() + " " + oauth2Response.getProviderId();
        Optional<Member> existData = memberRepository.findMemberWithAuth(username);
        UserDto userDto = new UserDto();
        if(existData.isEmpty()){
            Member member = new Member(username, oauth2Response.getName());
            Auth auth = authRepository.findByAuth(Role.USER);
            member.setAuth(auth);
            member.setEmail(oauth2Response.getEmail());
            memberRepository.save(member);
            return new CustomUser(member, true);
        }else{
            existData.get().setEmail(oauth2Response.getEmail());
            existData.get().setName(oauth2Response.getName());
            return new CustomUser(existData.get(), true);
        }
    }
}
