package side.chatting.dto;

import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import side.chatting.entity.Member;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@EqualsAndHashCode(of={"username"})
public class CustomUser implements UserDetails, OAuth2User {

    private final Member member;
    private String username;
    private String email;
    private String name;
    private boolean isOauth;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public CustomUser(Member member, boolean isOauth2) {
        this.member = member;
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.name = member.getName();
        this.isOauth = isOauth2;
    }



    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection collection = new ArrayList();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_USER";
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    public boolean isOauth(){
        return isOauth;
    }
}
