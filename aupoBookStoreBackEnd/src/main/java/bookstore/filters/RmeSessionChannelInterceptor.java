package bookstore.filters;

import bookstore.services.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.messaging.*;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.support.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.*;
import org.springframework.util.*;

import javax.servlet.http.*;
import java.text.*;
import java.util.*;

/**
 * Аутентификатор для токенов
 */
public class RmeSessionChannelInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtService jwtService;

    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    @Override
    @SuppressWarnings("unchecked")
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        System.out.println("Channel Interceptor " + message);

        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert accessor != null;

        Map<?, ?> nativeHeaders = (Map<?, ?>) message.getHeaders().get("nativeHeaders");
        if (nativeHeaders == null || nativeHeaders.get("Authorization") == null) {
            System.out.println("Without auth, simpSessionId: " + message.getHeaders().get("simpSessionId"));
            return message;
        }
        String jwt = parseJwt(((List<String>) nativeHeaders.get("Authorization")).stream().findFirst().orElse(null));

        if (jwt != null && jwtService.validateJwtToken(jwt)) {
            String username = jwtService.getUserNameFromJwtToken(jwt);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            //authentication.setDetails(new WebAuthenticationDetailsSource().bui);

            accessor.setUser(authentication);
            System.out.println(MessageFormat.format("User: {0}, simpSessionId: {1}", username, message.getHeaders().get("simpSessionId")));
        }

        return message;
    }

    private String parseJwt(String headerAuth) {

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}