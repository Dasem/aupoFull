package bookstore.auth;

import java.util.*;

public class AuthorizedUser {
    private String token;
    private String type = "Bearer";
    private String login;
    private final List<String> roles;

    public AuthorizedUser(String accessToken, String login, List<String> roles) {
        this.token = accessToken;
        this.login = login;
        this.roles = roles;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<String> getRoles() {
        return roles;
    }
}