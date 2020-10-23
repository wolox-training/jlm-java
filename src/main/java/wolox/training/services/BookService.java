package wolox.training.services;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import wolox.training.client.delegate.OpenLibraryDelegate;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.BookPreconditionFailedException;
import wolox.training.mappers.BookMapper;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final OpenLibraryDelegate openLibraryDelegate;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, OpenLibraryDelegate openLibraryDelegate,
        BookMapper bookMapper) {

        this.bookRepository = bookRepository;
        this.openLibraryDelegate = openLibraryDelegate;
        this.bookMapper = bookMapper;

    }

    public Iterable<Book> findAll(Map<String, String> params) {

        if (!params.isEmpty()) {

            if (params.get("isbn") != null) {

                return filterBookByIsbn(params.get("isbn"));

            } else {

                return findByAnyParameter(params);

            }

        }

        return bookRepository.findAll();

    }

    private Iterable<Book> findByAnyParameter(Map<String, String> params) {

        return bookRepository.findByAnyParameter(params.get("genre"), params.get("author"),
            params.get("image"), params.get("title"), params.get("subtitle"),
            params.get("publisher"), params.get("year"));

    }

    private Iterable<Book> filterBookByIsbn(String isbn) {

        Optional<Book> bookOptional = bookRepository.findByIsbn(isbn);
        if (bookOptional.isPresent()) {

            return Collections.singletonList(bookOptional.get());

        }

        Book bookFound = openLibraryDelegate.findBookByIsbn(isbn)
            .map(bookInfoDto -> bookMapper.bookInfoDtoToToEntity(isbn, bookInfoDto))
            .orElseThrow(BookNotFoundException::new);

        return Collections.singletonList(bookRepository.save(bookFound));

    }

    public Book findById(Long id) {

        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);

    }

    public Book save(Book book) {

        return bookRepository.save(book);

    }

    public Book update(Book book, Long id) {

        if (book.getId() != id) {
            throw new BookPreconditionFailedException();
        }

        bookRepository.findById(id).orElseThrow(BookNotFoundException::new);

        return bookRepository.save(book);

    }

    public void delete(Long id) {

        bookRepository.findById(id).orElseThrow(BookNotFoundException::new);

        bookRepository.deleteById(id);

    }
}
