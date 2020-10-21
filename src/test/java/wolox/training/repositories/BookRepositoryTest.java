package wolox.training.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.models.Book;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class BookRepositoryTest {

    private static final int PAGE_NUMBER = 90;
    private static final String HORROR_GENRE = "Horror";
    private static final String DEFAULT_AUTHOR = "Juan Osorio";
    private static final String DEFAULT_PUBLISHER = "Publisher";
    private static final String DEFAULT_YEAR = "2020";

    private Book bookToSave;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUpData() {

        // Arrange
        bookToSave = Book.builder().genre(HORROR_GENRE)
            .author(DEFAULT_AUTHOR)
            .image("horror.jpg")
            .title("Title")
            .subtitle("Subtitle")
            .publisher(DEFAULT_PUBLISHER)
            .year(DEFAULT_YEAR)
            .pages(PAGE_NUMBER)
            .isbn("0909-1234-6789-X")
            .build();

        bookRepository.save(bookToSave);

    }

    @Test
    void whenFindAll_thenReturnAllBooks() {

        // Act
        Iterable<Book> books = bookRepository.findAll();

        // Assert
        assertThat(books.iterator().hasNext()).isTrue();
        assertThat(books.iterator().next()).isEqualToComparingFieldByField(bookToSave);

    }

    @Test
    void whenFindByAuthor_thenReturnBook() {

        // Act
        Book bookFound = bookRepository.findByAuthor(DEFAULT_AUTHOR).get();

        // Assert
        assertThat(bookFound).isEqualToComparingFieldByField(bookToSave);

    }

    @Test
    void whenUpdate_thenReturnUpdatedBook() {

        // Act
        Book bookFoundOld = bookRepository.findByAuthor(DEFAULT_AUTHOR).get();
        bookFoundOld.setGenre("Comedy");

        bookRepository.save(bookFoundOld);
        Book bookFoundUpdated = bookRepository.findByAuthor(DEFAULT_AUTHOR).get();

        // Arrange
        assertThat(bookFoundUpdated.getGenre()).isEqualTo(bookFoundOld.getGenre());
        assertThat(bookFoundUpdated.getAuthor()).isEqualTo(bookFoundOld.getAuthor());
        assertThat(bookFoundUpdated.getIsbn()).isEqualTo(bookFoundOld.getIsbn());

    }

    @Test
    void whenDelete_thenReturnZeroBooks() {

        // Act
        Book bookFound = bookRepository.findByAuthor(DEFAULT_AUTHOR).get();

        bookRepository.delete(bookFound);

        Iterable<Book> books = bookRepository.findAll();

        // Assert
        assertThat(books.iterator().hasNext()).isFalse();

    }

    @Test
    void whenFindByGenreAndPublisherAndYear_thenReturnBookList() {

        // Arrange
        bookToSave = Book.builder().genre("SUSPENSE")
            .author(DEFAULT_AUTHOR)
            .image("horror.jpg")
            .title("Title")
            .subtitle("Subtitle")
            .publisher(DEFAULT_PUBLISHER)
            .year(DEFAULT_YEAR)
            .pages(PAGE_NUMBER)
            .isbn("0909-1234-6789-X")
            .build();

        bookRepository.save(bookToSave);

        // Act
        List<Book> books = bookRepository
            .findByGenreAndPublisherAndYear(HORROR_GENRE, DEFAULT_PUBLISHER, DEFAULT_YEAR);

        // Assert
        assertThat(books.isEmpty()).isFalse();
        assertThat(books.size()).isEqualTo(1);
        assertThat(books.get(0).getGenre()).isEqualTo(HORROR_GENRE);
        assertThat(books.get(0).getPublisher()).isEqualTo(DEFAULT_PUBLISHER);
        assertThat(books.get(0).getYear()).isEqualTo(DEFAULT_YEAR);

    }
}
