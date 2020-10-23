package wolox.training.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.authentication.IAuthentication;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.exceptions.UserPreconditionFailedException;
import wolox.training.models.Book;
import wolox.training.models.PasswordReset;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RestController
@RequestMapping("/api/users")
@Api
public class UserController {

    private final IAuthentication authentication;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(IAuthentication authentication, UserRepository userRepository,
        BookRepository bookRepository, PasswordEncoder passwordEncoder) {

        this.authentication = authentication;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.passwordEncoder = passwordEncoder;

    }

    /**
     * Method that gets authenticated user
     *
     * @return username (String)
     */
    @GetMapping("/me")
    public String authenticatedCurrentUser() {

        return authentication.getAuthentication().getName();

    }

    /**
     * Method that gets all users
     *
     * @return all {@link User}
     */
    @GetMapping
    @ApiOperation(value = "Return all users", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved all users")
    })
    public Iterable<User> findAll() {

        return userRepository.findAll();

    }

    /**
     * Method that obtain a user by id
     *
     * @param id: User identifier (Long)
     * @return {@link User}
     */
    @ApiOperation(value = "Given an id, return the user", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved user"),
        @ApiResponse(code = 404, message = "User not found")
    })
    @GetMapping("/{id}")
    public User findById(@ApiParam(value = "Id to find the user") @PathVariable Long id) {

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
    @ApiOperation(value = "Given a user, return same object", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully created user")
    })
    public User save(@RequestBody User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);

    }

    /**
     * User updating method
     *
     * @param user: Request body ({@link User})
     * @param id:   User identifier (Long)
     * @return {@link User}
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "Given an id and a user, return updated user", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully updated user"),
        @ApiResponse(code = 404, message = "User not found"),
        @ApiResponse(code = 412, message = "User's verification required")
    })
    public User update(@RequestBody User user,
        @ApiParam(value = "Id to find the user") @PathVariable Long id) {

        if (user.getId() != id) {
            throw new UserPreconditionFailedException();
        }

        User userFound = userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);

        user.setPassword(userFound.getPassword());

        return userRepository.save(user);

    }

    /**
     * Password update method
     *
     * @param id:        User identifier (Long)
     * @param passwords: Password request ({@link PasswordReset})
     */
    @PutMapping("/{id}/password")
    @ApiOperation(value = "Given an id, the password is updated")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Password updated"),
        @ApiResponse(code = 412, message = "User's verification required")
    })
    public void passwordReset(@ApiParam(value = "Id to find the user") @PathVariable Long id,
        @RequestBody PasswordReset passwords) {

        User userFound = userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);

        if (passwordEncoder.matches(passwords.getOldPassword(), userFound.getPassword())) {

            userFound.setPassword(passwordEncoder.encode(passwords.getNewPassword()));
            userRepository.save(userFound);

        } else {

            throw new UserPreconditionFailedException();

        }

    }

    /**
     * User deleting method
     *
     * @param id: User identifier (Long)
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Given an id, the user is deleted")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "User not found")
    })
    public void delete(@ApiParam(value = "Id to find the user") @PathVariable Long id) {

        userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);

        userRepository.deleteById(id);

    }

    /**
     * Method that adds a book
     *
     * @param idUser: User identifier (Long)
     * @param idBook: Book identifier (Long)
     * @return {@link User}
     */
    @PutMapping("/{idUser}/books/{idBook}")
    @ApiOperation(value = "Given an id user and id book, return user with the book added to your collection", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved user"),
        @ApiResponse(code = 404, message = "User not found"),
        @ApiResponse(code = 404, message = "Book not found")
    })
    public User addBook(@ApiParam(value = "Id to find the user") @PathVariable Long idUser,
        @ApiParam(value = "Id to find the book") @PathVariable Long idBook) {

        User userFound = userRepository.findById(idUser)
            .orElseThrow(UserNotFoundException::new);

        Book bookFound = bookRepository.findById(idBook)
            .orElseThrow(BookNotFoundException::new);

        userFound.addBook(bookFound);

        return userRepository.save(userFound);

    }

    /**
     * Method that removes a book
     *
     * @param idUser: User identifier (Long)
     * @param idBook: Book identifier (Long)
     * @return {@link User}
     */
    @DeleteMapping("/{idUser}/books/{idBook}")
    @ApiOperation(value = "Given an id user and id book, return user with the book deleted to your collection", response = User.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved user"),
        @ApiResponse(code = 404, message = "User not found"),
        @ApiResponse(code = 404, message = "Book not found")
    })
    public User removeBook(@ApiParam(value = "Id to find the user") @PathVariable Long idUser,
        @ApiParam(value = "Id to find the book") @PathVariable Long idBook) {

        User userFound = userRepository.findById(idUser)
            .orElseThrow(UserNotFoundException::new);

        Book bookFound = bookRepository.findById(idBook)
            .orElseThrow(BookNotFoundException::new);

        userFound.removeBook(bookFound);

        return userRepository.save(userFound);

    }

}
