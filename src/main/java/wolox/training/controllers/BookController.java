package wolox.training.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.models.Book;
import wolox.training.services.BookService;

@RestController
@RequestMapping("/api/books")
@Api
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {

        this.bookService = bookService;

    }

    /**
     * Method that gets all books
     *
     * @return all {@link Book}
     */
    @GetMapping
    @ApiOperation(value = "Return all books", response = Book.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved all books")
    })
    public Iterable<Book> findAll(@RequestParam(required = false) String isbn) {

        return bookService.findAll(isbn);

    }

    /**
     * Method that obtain a book by id
     *
     * @param id: Book identifier (Long)
     * @return {@link Book}
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "Given an id, return the book", response = Book.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved book"),
        @ApiResponse(code = 404, message = "Book not found")
    })
    public Book findById(@ApiParam(value = "Id to find the book") @PathVariable Long id) {

        return bookService.findById(id);

    }

    /**
     * Book saving method
     *
     * @param book: Request body ({@link Book})
     * @return {@link Book}
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Given a book, return same object", response = Book.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully created book")
    })
    public Book save(@RequestBody Book book) {

        return bookService.save(book);

    }

    /**
     * Book updating method
     *
     * @param book: Request body ({@link Book})
     * @param id:   Book identifier (Long)
     * @return {@link Book}
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "Given an id and a book, return updated book", response = Book.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully updated book"),
        @ApiResponse(code = 404, message = "Book not found"),
        @ApiResponse(code = 412, message = "Book's verification required")
    })
    public Book update(@RequestBody Book book,
        @ApiParam(value = "Id to find the book") @PathVariable Long id) {

        return bookService.update(book, id);

    }

    /**
     * Book deleting method
     *
     * @param id: Book identifier (Long)
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Given an id, the book is deleted")
    @ApiResponses(value = {
        @ApiResponse(code = 404, message = "Book not found")
    })
    public void delete(@ApiParam(value = "Id to find the book") @PathVariable Long id) {

        bookService.delete(id);

    }

}
