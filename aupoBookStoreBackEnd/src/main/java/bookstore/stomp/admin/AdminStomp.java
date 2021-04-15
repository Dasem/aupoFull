package bookstore.stomp.admin;

import bookstore.model.entities.*;
import bookstore.model.repositories.*;
import bookstore.stomp.utils.*;
import com.google.common.collect.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.annotation.*;
import org.springframework.security.access.annotation.*;
import org.springframework.security.crypto.password.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
@SendTo("/bookstore")
@MessageMapping("/admin")
public class AdminStomp {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    BookRepository bookRepository;

    @MessageMapping("/order")
    public StompResponse<Order> create(@RequestBody Order order) {
        StompResponse<Order> response = new StompResponse<>(RequestType.CREATE_ORDER);
        if (order.getBasket() == null) {

            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setCustomError("Basket is empty");
            return response;
        }
        if (order.getUser() == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setCustomError("User is null");
            return response;
        }
        if (order.getOrderDate() == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setCustomError("Order date is null");
            return response;
        }

        response.setContent(orderRepository.save(order));
        return response;
    }

    @MessageMapping("/order/{id}")
    public StompResponse<?> deleteOrder(@DestinationVariable long id) {
        orderRepository.deleteById(id);
        return new StompResponse<>(RequestType.DELETE_ORDER);
    }

    @MessageMapping("/get-users")
    public StompResponse<List<User>> getUsers() {
        return new StompResponse<>(userRepository.findAll(), RequestType.GET_USERS);
    }

    @MessageMapping("/orders")
    public StompResponse<List<Order>> orderList() {
        return new StompResponse<>(orderRepository.findAll(), RequestType.GET_ORDERS);
    }

    @MessageMapping("/books")
    public StompResponse<Book> saveOrUpdate(@RequestBody Book book) {
        StompResponse<Book> response = new StompResponse<>(RequestType.CREATE_BOOK);
        if (book.getAuthor() == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setCustomError("Author is null");
            return response;
        }
        if (book.getGenres() == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setCustomError("Genres is empty");
            return response;
        }
        if (book.getPrice() == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setCustomError("Price is null");
            return response;
        }
        if (book.getTitle() == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setCustomError("Title is null");
            return response;
        }
        response.setContent(bookRepository.save(book));
        return response;
    }

    @MessageMapping("/users")
    public StompResponse<User> saveOrUpdate(@RequestBody User user) {
        StompResponse<User> response = new StompResponse<>(RequestType.CREATE_USER);
        if (user.getLogin() == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setCustomError("Login is null");
            return response;
        }
        if (user.getPassword() == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setCustomError("Password is null");
            return response;
        }
        if (user.getRole() == null) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setCustomError("Role is null");
            return response;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        response.setContent(userRepository.save(user));
        return response;
    }

    @MessageMapping("/books/{id}")
    public StompResponse<?> deleteBook(@DestinationVariable long id) {
        bookRepository.deleteById(id);
        return new StompResponse<>(RequestType.DELETE_BOOK);
    }

    @MessageMapping("/users/{id}")
    public StompResponse<?> deleteUser(@DestinationVariable long id) {
        userRepository.deleteById(id);
        return new StompResponse<>(RequestType.DELETE_USER);
    }
}
