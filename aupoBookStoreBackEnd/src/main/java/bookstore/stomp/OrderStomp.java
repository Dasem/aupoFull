package bookstore.stomp;

import bookstore.model.entities.*;
import bookstore.model.entities.User;
import bookstore.model.repositories.*;
import bookstore.stomp.utils.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.core.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@SendTo("/bookstore")
public class OrderStomp {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @MessageMapping("/order")
    public StompResponse<Order> create(@RequestBody Order order, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findUserByLogin(userDetails.getUsername());
        order.setUser(user);
        order.setOrderDate(new Date());
        return new StompResponse<>(orderRepository.save(order), RequestType.SEND_USER_ORDER);
    }
}
