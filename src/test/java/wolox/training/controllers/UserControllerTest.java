package wolox.training.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

public class UserControllerTest {

    private static final String USER_PATH = "/api/users";
    private static final int USER_ID = 0;

    private MockMvc mockMvc;
    private UserRepository userRepository;
    private BookRepository bookRepository;
    private PasswordEncoder passwordEncoder;
    private User userTest;

    @BeforeEach
    void setUp() {

        // Arrange
        userRepository = mock(UserRepository.class);
        bookRepository = mock(BookRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);

        UserController userController = new UserController(userRepository, bookRepository,
            passwordEncoder);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        userTest = User.builder().username("test.username")
            .name("Test")
            .birthdate(LocalDate.now())
            .books(Collections.singletonList(Book.builder().genre("Comedy").build()))
            .build();

    }

    @Test
    void whenFindAll_thenReturnAllUsers() throws Exception {

        // Arrange
        when(userRepository.findAll()).thenReturn(Collections.singleton(userTest));

        // Act - Assert
        mockMvc.perform(get(USER_PATH)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("[0].username").value(userTest.getUsername()))
            .andExpect(jsonPath("[0].name").value(userTest.getName()));

    }

    @Test
    void whenFindById_thenReturnUser() throws Exception {

        // Arrange
        when(userRepository.findById(any())).thenReturn(Optional.of(userTest));

        // Act - Assert
        mockMvc.perform(get(USER_PATH + "/{id}", USER_ID)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("username").value(userTest.getUsername()))
            .andExpect(jsonPath("name").value(userTest.getName()));

    }

    @Test
    void whenFindById_thenReturnUserNotFound() throws Exception {

        // Arrange
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        // Act - Assert
        mockMvc.perform(get(USER_PATH + "/{id}", USER_ID)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print()).andExpect(status().isNotFound());

    }

    @Test
    void whenDelete_thenReturnIsOk() throws Exception {

        // Arrange
        when(userRepository.findById(any())).thenReturn(Optional.of(userTest));
        doNothing().when(userRepository).deleteById(any());

        // Act - Assert
        mockMvc.perform(delete(USER_PATH + "/{id}", USER_ID)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print()).andExpect(status().isOk());

    }

}
