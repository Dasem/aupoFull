package bookstore;

import bookstore.model.repositories.*;
import bookstore.stomp.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
public class SmokeTest {

    @Autowired
    BookStomp bookStomp;

    @Autowired
    GenreStomp genreStomp;

    @Autowired
    GenreRepository genreRepository;

    @Test
    public void bookControllerLoaded() {
        assertThat(bookStomp).isNotNull();
    }

    @Test
    public void genreControllerLoaded() {
        assertThat(genreStomp).isNotNull();
    }

    @Test
    public void genreRepositoryLoaded() {
        assertThat(genreRepository).isNotNull();
    }
}
