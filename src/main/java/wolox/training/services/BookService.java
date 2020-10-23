package wolox.training.services;

import java.util.Collections;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
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

    public Iterable<Book> findAll(String isbn) {

        if (StringUtils.isNotEmpty(isbn)) {

            return filterBookByIsbn(isbn);

        }

        return bookRepository.findAll();

    }

    private Iterable<Book> filterBookByIsbn(String isbn) {

        Optional<Book> bookOptional = bookRepository.findByIsbn(isbn);
        if (bookOptional.isPresent()) {

            return Collections.singleton(bookOptional.get());

        }

        Book bookFound = openLibraryDelegate.findBookByIsbn(isbn)
            .map(bookInfoDto -> bookMapper.bookInfoDtoToToEntity(isbn, bookInfoDto))
            .orElseThrow(BookNotFoundException::new);

        return Collections.singleton(bookRepository.save(bookFound));

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
