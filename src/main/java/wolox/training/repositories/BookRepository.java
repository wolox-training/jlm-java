package wolox.training.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wolox.training.models.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    /**
     * Method that obtain a book by its author
     *
     * @param author: Name of author (String)
     * @return {@link Book}
     */
    Optional<Book> findByAuthor(String author);

    /**
     * Method that obtain all books by your genre, publisher and year
     *
     * @param genre:     Genre of the book (String)
     * @param publisher: Publisher of the book (String)
     * @param year:      Year of the book (String)
     * @return {@link Book} list
     */
    @Query("SELECT b FROM Book b WHERE b.genre = :genre and b.publisher = :publisher and b.year = :year")
    List<Book> findByGenreAndPublisherAndYearCustom(@Param("genre") String genre,
        @Param("publisher") String publisher, @Param("year") String year);

    /**
     * Method that obtain a book by its isbn
     *
     * @param isbn: Isbn of book (String)
     * @return {@link Book}
     */
    Optional<Book> findByIsbn(String isbn);

}
