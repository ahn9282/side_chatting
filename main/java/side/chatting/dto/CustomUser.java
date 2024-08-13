package side.chatting.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import side.chatting.entity.Member;
import side.chatting.entity.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@EqualsAndHashCode(of={"username"})
@ToString
public class CustomUser implements UserDetails, OAuth2User {

    @Getter
    private Long memberId;
    private String username;
    private String password;
    @Getter
    private String email;
    @Getter
    private String name;
    private boolean isOauth;
    private Role role;

    public CustomUser(Member member, boolean isOauth2) {
        this.username = member.getUsername();
        this.name = member.getName();
        this.isOauth = isOauth2;
        this.memberId = member.getId();
        this.password = member.getPassword();
        this.role = member.getAuth().getAuth();
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
                return role.getAuth();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return password;
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
