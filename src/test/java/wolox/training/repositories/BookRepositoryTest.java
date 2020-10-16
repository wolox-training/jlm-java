package wolox.training.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

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
    private static final String AUTHOR = "Juan Osorio";

    private Book bookToSave;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUpData() {

        // Arrange
        bookToSave = Book.builder().genre("Horror")
            .author(AUTHOR)
            .image("horror.jpg")
            .title("Title")
            .subtitle("Subtitle")
            .publisher("Publisher")
            .year("2020")
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
        Book bookFound = bookRepository.findByAuthor(AUTHOR).get();

        // Assert
        assertThat(bookFound).isEqualToComparingFieldByField(bookToSave);

    }

    @Test
    void whenUpdate_thenReturnUpdatedBook() {

        // Act
        Book bookFoundOld = bookRepository.findByAuthor(AUTHOR).get();
        bookFoundOld.setGenre("Comedy");

        bookRepository.save(bookFoundOld);
        Book bookFoundUpdated = bookRepository.findByAuthor(AUTHOR).get();

        // Arrange
        assertThat(bookFoundUpdated.getGenre()).isEqualTo(bookFoundOld.getGenre());
        assertThat(bookFoundUpdated.getAuthor()).isEqualTo(bookFoundOld.getAuthor());
        assertThat(bookFoundUpdated.getIsbn()).isEqualTo(bookFoundOld.getIsbn());

    }

    @Test
    void whenDelete_thenReturnZeroBooks() {

        // Act
        Book bookFound = bookRepository.findByAuthor(AUTHOR).get();

        bookRepository.delete(bookFound);

        Iterable<Book> books = bookRepository.findAll();

        // Assert
        assertThat(books.iterator().hasNext()).isFalse();

    }
}
