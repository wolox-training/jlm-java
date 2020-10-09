package wolox.training.repositories;

import java.util.Optional;
import org.springframework.data.repository.Repository;
import wolox.training.models.Book;

@org.springframework.stereotype.Repository
public interface BookRepository extends Repository<Book, Long> {

    /**
     * Method that obtain a book by its author
     * @param author: Name of author (String)
     * @return {@link Book}
     */
    Optional<Book> findByAuthor(String author);

}
