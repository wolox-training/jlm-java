package wolox.training.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.exceptions.UserPreconditionFailedException;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {

        this.userRepository = userRepository;

    }

    /**
     * Method that gets all users
     *
     * @return all {@link User}
     */
    @GetMapping
    public Iterable<User> findAll() {

        return userRepository.findAll();

    }

    /**
     * Method that obtain a user by id
     *
     * @param id: User identifier (Long)
     * @return {@link User}
     */
    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {

        return userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);

    }

    /**
     * User saving method
     *
     * @param user: Request body ({@link User})
     * @return {@link User}
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User save(@RequestBody User user) {

        return userRepository.save(user);

    }

    /**
     * User updating method
     *
     * @param user: Request body ({@link User})
     * @param id: User identifier (Long)
     * @return {@link User}
     */
    @PutMapping("/{id}")
    public User update(@RequestBody User user, @PathVariable Long id) {

        if (user.getId() != id) {
            throw new UserPreconditionFailedException();
        }

        userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);

        return userRepository.save(user);

    }

    /**
     * User deleting method
     *
     * @param id: User identifier (Long)
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);

        userRepository.deleteById(id);

    }

}
