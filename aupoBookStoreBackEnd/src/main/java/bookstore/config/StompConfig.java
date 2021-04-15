package bookstore.config;

import bookstore.filters.*;
import org.springframework.context.annotation.*;
import org.springframework.http.server.*;
import org.springframework.messaging.simp.config.*;
import org.springframework.web.socket.*;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.*;
import org.springframework.web.socket.server.support.*;

import javax.servlet.http.*;
import java.security.*;
import java.util.*;

@Configuration
@EnableWebSocketMessageBroker
public class StompConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/bookstore");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/bs")
                .setAllowedOrigins("*")
                .withSockJS();
        registry.setErrorHandler(new ErrorHandler());
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(rmeSessionChannelInterceptor());
    }

    @Bean
    public RmeSessionChannelInterceptor rmeSessionChannelInterceptor() {
        return new RmeSessionChannelInterceptor();
    }
}