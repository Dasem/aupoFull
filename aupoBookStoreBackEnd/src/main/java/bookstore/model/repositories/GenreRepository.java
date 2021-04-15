package bookstore.model.repositories;

import bookstore.model.entities.Genre;
import org.springframework.data.jpa.repository.*;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
