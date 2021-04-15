package bookstore.model.repositories;

import bookstore.model.entities.Book;
import org.springframework.data.jpa.repository.*;

public interface BookRepository extends JpaRepository<Book, Long> {
}
