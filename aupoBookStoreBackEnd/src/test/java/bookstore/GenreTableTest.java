package bookstore;


import bookstore.model.entities.*;
import bookstore.model.repositories.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class GenreTableTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    GenreRepository genreRepository;

    @Test
    public void shouldFindAllGenres() {
        Iterable<Genre> genres = genreRepository.findAll();

        assertThat(genres).isNotEmpty();
    }

    @Test
    public void shouldInsertGenres() {

        Genre savedGenre = this.genreRepository.save(new Genre("Test genre"));

        assertThat(savedGenre).hasFieldOrPropertyWithValue("name", "Test genre");
    }

    @Test
    public void shouldFindById() {
        Genre savedGenre = this.genreRepository.save(new Genre("Test genre"));
        Optional<Genre> foundGenre = this.genreRepository.findById(savedGenre.getId());

        assertThat(foundGenre.get()).isNotNull();
        assertThat(foundGenre.get()).hasFieldOrPropertyWithValue("name", "Test genre");
    }

    @Test
    public void shouldDelete() {
        Genre savedGenre = this.genreRepository.save(new Genre("Test genre"));
        this.genreRepository.delete(savedGenre);
        Optional<Genre> foundGenre = this.genreRepository.findById(savedGenre.getId());

        assertThat(foundGenre).isEmpty();
    }

}
