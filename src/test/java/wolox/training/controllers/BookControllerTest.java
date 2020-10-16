package wolox.training.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

class BookControllerTest {

    private static final String BOOK_PATH = "/api/books";
    private static final int BOOK_ID = 0;
    private static final int INVALID_BOOK_ID = 1;
    private static final int PAGE_NUMBER = 90;

    private MockMvc mockMvc;
    private BookRepository bookRepository;
    private ObjectMapper objectMapper;
    private Book bookTest;

    @BeforeEach
    void setUp() {

        // Arrange
        objectMapper = new ObjectMapper();
        bookRepository = mock(BookRepository.class);

        BookController bookController = new BookController(bookRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();

        bookTest = Book.builder().genre("Horror")
            .author("Juan Osorio")
            .image("horror.jpg")
            .title("Title")
            .subtitle("Subtitle")
            .publisher("Publisher")
            .year("2020")
            .pages(PAGE_NUMBER)
            .isbn("0909-1234-6789-X")
            .build();

    }

    @Test
    void whenFindAll_thenReturnAllBooks() throws Exception {

        // Arrange
        when(bookRepository.findAll()).thenReturn(Collections.singleton(bookTest));

        // Act - Assert
        mockMvc.perform(get(BOOK_PATH)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("[0].genre").value(bookTest.getGenre()))
            .andExpect(jsonPath("[0].author").value(bookTest.getAuthor()))
            .andExpect(jsonPath("[0].title").value(bookTest.getTitle()))
            .andExpect(jsonPath("[0].subtitle").value(bookTest.getSubtitle()));

    }

    @Test
    void whenFindById_thenReturnBook() throws Exception {

        // Arrange
        when(bookRepository.findById(any())).thenReturn(Optional.of(bookTest));

        // Act - Assert
        mockMvc.perform(get(BOOK_PATH + "/{id}", BOOK_ID)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("publisher").value(bookTest.getPublisher()))
            .andExpect(jsonPath("year").value(bookTest.getYear()))
            .andExpect(jsonPath("pages").value(bookTest.getPages()))
            .andExpect(jsonPath("isbn").value(bookTest.getIsbn()));

    }

    @Test
    void whenFindById_thenReturnBookNotFound() throws Exception {

        // Arrange
        when(bookRepository.findById(any())).thenReturn(Optional.empty());

        // Act - Assert
        mockMvc.perform(get(BOOK_PATH + "/{id}", BOOK_ID)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print()).andExpect(status().isNotFound());

    }

    @Test
    void whenSave_thenReturnSavedBook() throws Exception {

        // Arrange
        when(bookRepository.save(any())).thenReturn(bookTest);

        // Act - Assert
        mockMvc.perform(post(BOOK_PATH)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(bookTest)))
            .andDo(print()).andExpect(status().isCreated())
            .andExpect(jsonPath("genre").value(bookTest.getGenre()))
            .andExpect(jsonPath("author").value(bookTest.getAuthor()))
            .andExpect(jsonPath("title").value(bookTest.getTitle()))
            .andExpect(jsonPath("subtitle").value(bookTest.getSubtitle()));

    }

    @Test
    void whenUpdate_thenReturnUpdatedBook() throws Exception {

        // Arrange
        when(bookRepository.findById(any())).thenReturn(Optional.of(bookTest));
        when(bookRepository.save(any())).thenReturn(bookTest);

        // Act - Assert
        mockMvc.perform(put(BOOK_PATH + "/{id}", BOOK_ID)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(bookTest)))
            .andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("genre").value(bookTest.getGenre()))
            .andExpect(jsonPath("author").value(bookTest.getAuthor()))
            .andExpect(jsonPath("title").value(bookTest.getTitle()))
            .andExpect(jsonPath("subtitle").value(bookTest.getSubtitle()));

    }

    @Test
    void whenUpdate_thenReturnUpdatedBookPreconditionFailed() throws Exception {

        // Arrange
        when(bookRepository.findById(any())).thenReturn(Optional.of(bookTest));
        when(bookRepository.save(any())).thenReturn(bookTest);

        // Act - Assert
        mockMvc.perform(put(BOOK_PATH + "/{id}", INVALID_BOOK_ID)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(bookTest)))
            .andDo(print()).andExpect(status().isPreconditionFailed());

    }

    @Test
    void whenDelete_thenReturnDeletedBook() throws Exception {

        // Arrange
        when(bookRepository.findById(any())).thenReturn(Optional.of(bookTest));
        doNothing().when(bookRepository).deleteById(any());

        // Act - Assert
        mockMvc.perform(delete(BOOK_PATH + "/{id}", BOOK_ID)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print()).andExpect(status().isOk());

    }

}
