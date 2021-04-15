package bookstore.stomp.security;

import bookstore.auth.*;
import bookstore.model.entities.*;
import bookstore.model.entities.User;
import bookstore.model.repositories.*;
import bookstore.services.*;
import bookstore.stomp.utils.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.*;

@RestController
@CrossOrigin
@SendTo("/bookstore")
public class AuthorisationStomp {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @MessageMapping("/signin")
    public StompResponse<AuthorizedUser> authenticateUser(@RequestBody User signInRequest) {
        AuthorizedUser authorizedUser = authorize(signInRequest.getLogin(), signInRequest.getPassword());
        if (authorizedUser == null) {
            StompResponse<AuthorizedUser> badCredentials = new StompResponse<>(null, RequestType.SIGN_IN);
            badCredentials.setStatus(HttpStatus.UNAUTHORIZED.value());
            badCredentials.setCustomError("Bad credentials");
            return badCredentials;
        }
        return new StompResponse<>(authorizedUser, RequestType.SIGN_IN);
    }

    @MessageMapping("/signup")
    public StompResponse<AuthorizedUser> registerUser(@RequestBody User signUpRequest) {
        if (userRepository.findUserByLogin(signUpRequest.getLogin()) != null) {
            StompResponse<AuthorizedUser> response = new StompResponse<>(RequestType.SIGN_UP);
            response.setStatus(HttpStatus.CONFLICT.value());
            response.setCustomError("Username is already taken!");
            return response;
        }

        signUpRequest.setId(null);
        signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        signUpRequest.setRole(Role.ROLE_USER);
        userRepository.save(signUpRequest);

        return new StompResponse<>(RequestType.SIGN_UP);
    }

    @MessageMapping("/setup-token")
    public StompResponse<AuthorizedUser> setupToken(@RequestBody User signUpRequest) {
        if (userRepository.findUserByLogin(signUpRequest.getLogin()) != null) {
            StompResponse<AuthorizedUser> response = new StompResponse<>(RequestType.SIGN_UP);
            response.setStatus(HttpStatus.CONFLICT.value());
            response.setCustomError("Username is already taken!");
            return response;
        }

        signUpRequest.setId(null);
        signUpRequest.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        signUpRequest.setRole(Role.ROLE_USER);
        userRepository.save(signUpRequest);

        return new StompResponse<>(RequestType.SIGN_UP);
    }

    private AuthorizedUser authorize(String login, String password) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login, password));
        } catch (BadCredentialsException ex) {
            return null;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new AuthorizedUser(jwt,
                userDetails.getUsername(),
                roles);
    }
}