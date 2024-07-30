package side.chatting.entity;

import org.apache.coyote.BadRequestException;

public enum Role {
    USER("ROLE_USER"), ADMIN("ROLE_ADMIN") ;

    Role(String role) {
        this.authority = role;
    }

    private final String authority;

    public String getAuth() {
        return authority;
    }

    public Role check(String authority) {
        for (Role role : Role.values()) {
            if(role.getAuth().equals(authority)) return role;
        }
        throw new IllegalArgumentException("Invalid authority [ your input : " + authority + "]");
    }
}
