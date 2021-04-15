package bookstore.stomp;

import bookstore.model.entities.*;
import bookstore.model.repositories.*;
import bookstore.stomp.utils.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.messaging.*;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.*;
import org.springframework.messaging.simp.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.security.*;
import java.util.*;

@RestController
@CrossOrigin
@SendTo("/bookstore")
public class BookStomp {

    @Autowired
    BookRepository books;

    /**
     * полный список - GET
     */
    @MessageMapping("/books")
    public StompResponse<List<Book>> index() {
        return new StompResponse<>(books.findAll(), RequestType.GET_BOOKS);
    }
}
