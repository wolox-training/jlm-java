package wolox.training.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.models.User;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class UserRepositoryTest {

    private static final String USERNAME = "test.username";

    private User userToSave;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUpData() {

        // Arrange
        userToSave = User.builder().username(USERNAME)
            .password("12345")
            .name("Test")
            .birthdate(LocalDate.now())
            .build();

        userRepository.save(userToSave);

    }

    @Test
    void whenFindAll_thenReturnAllUsers() {

        // Act
        Iterable<User> users = userRepository.findAll();

        // Assert
        assertThat(users.iterator().hasNext()).isTrue();
        assertThat(users.iterator().next()).isEqualToComparingFieldByField(userToSave);

    }

    @Test
    void whenFindByUsername_thenReturnUser() {

        // Act
        User userFound = userRepository.findByUsername(USERNAME).get();

        // Assert
        assertThat(userFound).isEqualToComparingFieldByField(userToSave);

    }

    @Test
    void whenUpdate_thenReturnUpdatedUser() {

        // Act
        User userFoundOld = userRepository.findByUsername(USERNAME).get();
        userFoundOld.setName("Test Test");

        userRepository.save(userFoundOld);
        User userFoundUpdated = userRepository.findByUsername(USERNAME).get();

        // Arrange
        assertThat(userFoundUpdated.getName()).isEqualTo(userFoundOld.getName());
        assertThat(userFoundUpdated.getUsername()).isEqualTo(userFoundOld.getUsername());
        assertThat(userFoundUpdated.getBirthdate()).isEqualTo(userFoundOld.getBirthdate());

    }

    @Test
    void whenDelete_thenReturnZeroBooks() {

        // Act
        User userFound = userRepository.findByUsername(USERNAME).get();

        userRepository.delete(userFound);

        Iterable<User> users = userRepository.findAll();

        // Assert
        assertThat(users.iterator().hasNext()).isFalse();

    }

}
