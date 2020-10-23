package wolox.training.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import wolox.training.client.delegate.OpenLibraryDelegate;
import wolox.training.client.models.BookInfoDto;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.BookPreconditionFailedException;
import wolox.training.mappers.BookMapper;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    private static final PodamFactory FACTORY = new PodamFactoryImpl();
    private static final Long BOOK_ID = 0L;
    private static final Long INVALID_BOOK_ID = 1L;

    private Book bookTest;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private OpenLibraryDelegate openLibraryDelegate;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService testClass;

    @BeforeEach
    void setUp() {

        // Arrange
        bookTest = Book.builder().genre("Horror")
            .author("Juan Osorio")
            .image("horror.jpg")
            .title("Title")
            .subtitle("Subtitle")
            .publisher("Publisher")
            .year("2020")
            .pages(90)
            .isbn("0909-1234-6789-X")
            .build();

    }

    @Test
    void whenFindAll_thenReturnAllBooks() {

        // Arrange
        when(bookRepository.findAll()).thenReturn(Collections.singletonList(bookTest));

        // Act
        Iterable<Book> books = testClass.findAll(new HashMap<>());

        // Assert
        assertThat(books.iterator().hasNext()).isTrue();
        assertThat(books.iterator().next().getIsbn()).isEqualTo(bookTest.getIsbn());
        assertThat(books.iterator().next().getAuthor()).isEqualTo(bookTest.getAuthor());
        assertThat(books.iterator().next().getGenre()).isEqualTo(bookTest.getGenre());
        assertThat(books.iterator().next().getImage()).isEqualTo(bookTest.getImage());

    }

    @Test
    void whenFindByIsbnInDB_thenReturnBook() {

        // Arrange
        Map<String, String> params = new HashMap<>();
        params.put("isbn", "7892189121-A");

        when(bookRepository.findByIsbn(any())).thenReturn(Optional.of(bookTest));

        // Act
        Iterable<Book> books = testClass.findAll(params);

        // Assert
        assertThat(books.iterator().hasNext()).isTrue();
        assertThat(books.iterator().next().getIsbn()).isEqualTo(bookTest.getIsbn());
        assertThat(books.iterator().next().getAuthor()).isEqualTo(bookTest.getAuthor());
        assertThat(books.iterator().next().getGenre()).isEqualTo(bookTest.getGenre());
        assertThat(books.iterator().next().getImage()).isEqualTo(bookTest.getImage());

    }

    @Test
    void whenFindByIsbnInExternalApi_thenReturnBook() {

        // Arrange
        Map<String, String> params = new HashMap<>();
        params.put("isbn", "7892189121-A");

        BookInfoDto bookInfoDto = FACTORY.manufacturePojo(BookInfoDto.class);

        when(bookRepository.findByIsbn(any())).thenReturn(Optional.empty());
        when(openLibraryDelegate.findBookByIsbn(any())).thenReturn(Optional.of(bookInfoDto));
        when(bookMapper.bookInfoDtoToToEntity(any(), any())).thenReturn(bookTest);

        // Act
        Iterable<Book> books = testClass.findAll(params);

        // Assert
        assertThat(books.iterator().hasNext()).isTrue();

    }

    @Test
    void whenFindByIsbnInExternalApi_thenReturnBookNotFound() {

        // Arrange
        String isbn = "7892189121-A";
        Map<String, String> params = new HashMap<>();
        params.put("isbn", isbn);

        when(bookRepository.findByIsbn(any())).thenReturn(Optional.empty());
        when(openLibraryDelegate.findBookByIsbn(any())).thenReturn(Optional.empty());

        // Act
        assertThatThrownBy(() -> testClass.findAll(params))
            .isInstanceOf(BookNotFoundException.class);

    }

    @Test
    void whenFindByAnyParameter_thenReturnBookList() {

        // Arrange
        Map<String, String> params = new HashMap<>();
        params.put("genre", "Horror");

        when(bookRepository.findByAnyParameter(any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(Collections.singletonList(bookTest));

        // Act
        Iterable<Book> books = testClass.findAll(params);

        // Assert
        assertThat(books.iterator().hasNext()).isTrue();
        assertThat(books.iterator().next().getIsbn()).isEqualTo(bookTest.getIsbn());
        assertThat(books.iterator().next().getAuthor()).isEqualTo(bookTest.getAuthor());
        assertThat(books.iterator().next().getGenre()).isEqualTo(bookTest.getGenre());
        assertThat(books.iterator().next().getImage()).isEqualTo(bookTest.getImage());

    }

    @Test
    void whenFindById_thenReturnBook() {

        // Arrange
        when(bookRepository.findById(any())).thenReturn(Optional.of(bookTest));

        // Act
        Book book = testClass.findById(BOOK_ID);

        // Assert
        assertThat(book.getIsbn()).isEqualTo(bookTest.getIsbn());
        assertThat(book.getAuthor()).isEqualTo(bookTest.getAuthor());
        assertThat(book.getGenre()).isEqualTo(bookTest.getGenre());
        assertThat(book.getImage()).isEqualTo(bookTest.getImage());

    }

    @Test
    void whenFindById_thenReturnBookNotFound() {

        // Arrange
        when(bookRepository.findById(any())).thenReturn(Optional.empty());

        // Act - Assert
        assertThatThrownBy(() -> testClass.findById(BOOK_ID))
            .isInstanceOf(BookNotFoundException.class);

    }

    @Test
    void whenSave_thenReturnSavedBook() {

        // Arrange
        when(bookRepository.save(any())).thenReturn(bookTest);

        // Act
        Book book = testClass.save(bookTest);

        // Assert
        assertThat(book.getIsbn()).isEqualTo(bookTest.getIsbn());
        assertThat(book.getAuthor()).isEqualTo(bookTest.getAuthor());
        assertThat(book.getGenre()).isEqualTo(bookTest.getGenre());
        assertThat(book.getImage()).isEqualTo(bookTest.getImage());

    }

    @Test
    void whenUpdate_thenReturnUpdatedBook() {

        // Arrange
        when(bookRepository.findById(any())).thenReturn(Optional.of(bookTest));
        when(bookRepository.save(any())).thenReturn(bookTest);

        // Act
        Book book = testClass.update(bookTest, BOOK_ID);

        // Assert
        assertThat(book.getIsbn()).isEqualTo(bookTest.getIsbn());
        assertThat(book.getAuthor()).isEqualTo(bookTest.getAuthor());
        assertThat(book.getGenre()).isEqualTo(bookTest.getGenre());
        assertThat(book.getImage()).isEqualTo(bookTest.getImage());

    }

    @Test
    void whenUpdate_thenReturnUpdatedBookPreconditionFailed() {

        // Act - Assert
        assertThatThrownBy(() -> testClass.update(bookTest, INVALID_BOOK_ID))
            .isInstanceOf(BookPreconditionFailedException.class);

    }

    @Test
    void whenDelete_thenReturnDeletedBook() {

        // Arrange
        when(bookRepository.findById(any())).thenReturn(Optional.of(bookTest));
        doNothing().when(bookRepository).deleteById(any());

        // Act
        testClass.delete(BOOK_ID);

        // Assert
        verify(bookRepository, times(1)).deleteById(BOOK_ID);

    }

}
