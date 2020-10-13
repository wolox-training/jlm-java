package wolox.training.models;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wolox.training.exceptions.BookAlreadyOwnedException;

@Table(name = "users")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", initialValue = 1)
    @Setter(AccessLevel.NONE)
    private long id;

    @NotNull
    private String username;

    @NotNull
    private String name;

    @NotNull
    private LocalDate birthdate;

    @NotNull
    @ManyToMany(mappedBy = "users")
    private List<Book> books = Collections.emptyList();

    public List<Book> getBooks() {

        return Collections.unmodifiableList(books);

    }

    public void addBook(Book book) {

        if (this.books.contains(book)) {
            throw new BookAlreadyOwnedException();
        }

        this.books.add(book);

    }

    public boolean removeBook(Book book) {

        return books.remove(book);

    }

}
