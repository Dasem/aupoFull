package bookstore.model.entities;

import org.springframework.security.core.*;

public enum Role implements GrantedAuthority {

    ROLE_ADMIN(Code.ROLE_ADMIN),
    ROLE_USER(Code.ROLE_USER),
    ROLE_ANONYMOUS(Code.ROLE_ANONYMOUS);

    /** type saved roles */
    public interface Code {
        String ROLE_ADMIN = "ROLE_ADMIN";
        String ROLE_USER = "ROLE_USER";
        String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    }

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}