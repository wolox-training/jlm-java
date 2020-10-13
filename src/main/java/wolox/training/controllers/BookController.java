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
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.BookPreconditionFailedException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {

        this.bookRepository = bookRepository;

    }

    /**
     * Method that gets all books
     *
     * @return all {@link Book}
     */
    @GetMapping
    public Iterable<Book> findAll() {

        return bookRepository.findAll();

    }

    /**
     * Method that obtain a book by id
     *
     * @param id: Book identifier (Long)
     * @return {@link Book}
     */
    @GetMapping("/{id}")
    public Book findById(@PathVariable Long id) {

        return bookRepository.findById(id)
            .orElseThrow(BookNotFoundException::new);

    }

    /**
     * Book saving method
     *
     * @param book: Request body ({@link Book})
     * @return {@link Book}
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book save(@RequestBody Book book) {

        return bookRepository.save(book);

    }

    /**
     * Book updating method
     *
     * @param book: Request body ({@link Book})
     * @param id: Book identifier (Long)
     * @return {@link Book}
     */
    @PutMapping("/{id}")
    public Book update(@RequestBody Book book, @PathVariable Long id) {

        if (book.getId() != id) {
            throw new BookPreconditionFailedException();
        }

        bookRepository.findById(id)
            .orElseThrow(BookNotFoundException::new);

        return bookRepository.save(book);

    }

    /**
     * Book deleting method
     *
     * @param id: Book identifier (Long)
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        bookRepository.findById(id)
            .orElseThrow(BookNotFoundException::new);

        bookRepository.deleteById(id);

    }

}
