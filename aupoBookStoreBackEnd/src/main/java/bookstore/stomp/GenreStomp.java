package bookstore.stomp;

import bookstore.model.entities.Genre;
import bookstore.model.repositories.GenreRepository;
import bookstore.stomp.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@SendTo("/bookstore")
public class GenreStomp {

    @Autowired
    GenreRepository genres;

    @MessageMapping("/genres")
    public StompResponse<List<Genre>> index() {
        return new StompResponse<>(genres.findAll(), RequestType.GET_GENRES);
    }
}
