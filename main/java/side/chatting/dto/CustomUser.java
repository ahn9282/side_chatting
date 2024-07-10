package side.chatting.dto;

import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import side.chatting.entity.Member;

import java.util.ArrayList;
import java.util.Collection;

@EqualsAndHashCode(of={"username"})
public class CustomUser implements UserDetails {

    private final Member member;
    private String username;
    private String email;
    private String name;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public CustomUser(Member member) {
        this.member = member;
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.name = member.getName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection collection = new ArrayList();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return String.valueOf(member.getAuth().getAuth());
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
}
