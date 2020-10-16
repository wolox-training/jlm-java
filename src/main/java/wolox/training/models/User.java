package wolox.training.models;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wolox.training.exceptions.BookAlreadyOwnedException;

@Table(name = "users")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", initialValue = 1)
    private long id;

    @NotNull
    @ApiModelProperty(required = true, notes = "Username of the user", example = "jaime.morales")
    private String username;

    @NotNull
    @ApiModelProperty(required = true, notes = "Name of the user", example = "Jaime Morales")
    private String name;

    @NotNull
    @ApiModelProperty(required = true, notes = "Birthdate of the user", example = "2020/10/15")
    private LocalDate birthdate;

    @Setter
    @ManyToMany
    @ApiModelProperty(notes = "User books")
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

    public void setUsername(String username) {

        checkNotNull(username, "Please check username field, its null");

        this.username = username;

    }

    public void setName(String name) {

        checkNotNull(name, "Please check name field, its null");

        this.name = name;

    }

    public void setBirthdate(LocalDate birthdate) {

        checkNotNull(birthdate, "Please check birthdate field, its null");
        checkArgument(birthdate.isBefore(LocalDate.now()),
            "Please check birthdate field, its can't be today or greater than today");

        this.birthdate = birthdate;

    }

}
