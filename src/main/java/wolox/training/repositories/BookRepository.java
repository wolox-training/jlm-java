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
    List<Book> findByGenreAndPublisherAndYear(String genre, String publisher, String year);

    /**
     * Method that obtain a book by its isbn
     *
     * @param isbn: Isbn of book (String)
     * @return {@link Book}
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Method that obtain all books by any parameters
     *
     * @param genre:     Genre of the book (String)
     * @param author:    Author of the book (String)
     * @param image:     Image of the book (String)
     * @param title:     Title of the book (String)
     * @param subtitle:  Subtitle of the book (String)
     * @param publisher: Publisher of the book (String)
     * @param year:      Year of the book (String)
     * @return {@link Book} list
     */
    @Query("SELECT b FROM Book b where (:genre is null or b.genre = :genre) "
        + "and (:author is null or b.author = :author) "
        + "and (:image is null or b.image = :image) "
        + "and (:title is null or b.title = :title) "
        + "and (:subtitle is null or b.subtitle = :subtitle) "
        + "and (:publisher is null or b.publisher = :publisher) "
        + "and (:year is null or b.year = :year)")
    List<Book> findByAnyParameter(
        @Param("genre") String genre,
        @Param("author") String author,
        @Param("image") String image,
        @Param("title") String title,
        @Param("subtitle") String subtitle,
        @Param("publisher") String publisher,
        @Param("year") String year);

}
