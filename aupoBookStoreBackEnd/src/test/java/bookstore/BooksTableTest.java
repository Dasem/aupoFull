package bookstore;

import bookstore.model.entities.*;
import bookstore.model.repositories.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.*;
import org.springframework.test.context.jdbc.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

//todo: если будет не влом, протестить стомпы https://medium.com/@MelvinBlokhuijzen/spring-websocket-endpoints-integration-testing-180357b4f24c

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class BooksTableTest {

    @Autowired
    BookRepository bookRepository;

    @Test
    public void isCorrectFilled() {
        List<Book> books = bookRepository.findAll();
        assertThat(books.size()).isEqualTo(5);
    }
}
