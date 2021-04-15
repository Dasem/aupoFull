package bookstore.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.messaging.*;
import org.springframework.security.config.annotation.web.socket.*;

@Configuration
public class SocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpDestMatchers("/admin/**").hasRole("ADMIN")
                .simpDestMatchers("/order").hasAnyRole("USER", "ADMIN")
        ;
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
