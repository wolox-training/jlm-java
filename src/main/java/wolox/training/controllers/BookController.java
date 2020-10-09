package wolox.training.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {

        this.bookRepository = bookRepository;

    }

    @GetMapping()
    public Iterable<Book> findAll() {

        return bookRepository.findAll();

    }

    @GetMapping("/{id}")
    public Book findById(@PathVariable long id) {

        return bookRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

    }

    @GetMapping("/{author}")
    public Book findByAuthor(@PathVariable String author) {

        return bookRepository.findByAuthor(author)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book save(@RequestBody Book book) {

        return bookRepository.save(book);

    }

    @PutMapping("/{id}")
    public Book update(@RequestBody Book book, @PathVariable long id) {

        if (book.getId() != id) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED,
                "Book's verification required");
        }

        bookRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        return bookRepository.save(book);

    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable long id) {

        bookRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        bookRepository.deleteById(id);
        return true;

    }

}
