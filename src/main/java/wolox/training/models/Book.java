package wolox.training.models;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Collections;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@EqualsAndHashCode(exclude = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Training API book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq")
    @SequenceGenerator(name = "book_seq", initialValue = 1)
    private long id;

    @ApiModelProperty(example = "Horror")
    private String genre;

    @NotNull
    @ApiModelProperty(required = true, notes = "Author of the book", example = "Jaime Morales")
    private String author;

    @NotNull
    @ApiModelProperty(required = true, notes = "Image of the book", example = "https:comedy.jpg")
    private String image;

    @NotNull
    @ApiModelProperty(required = true, notes = "Title of the book", example = "It is better not to breathe")
    private String title;

    @NotNull
    @ApiModelProperty(required = true, notes = "Subtitle of the book", example = "The night is just the beginning.")
    private String subtitle;

    @NotNull
    @ApiModelProperty(required = true, notes = "Publisher of the book", example = "Norma")
    private String publisher;

    @NotNull
    @ApiModelProperty(required = true, notes = "Year of the book", example = "2020")
    private String year;

    @NotNull
    @ApiModelProperty(required = true, notes = "Page number of the book", example = "300")
    private int pages;

    @NotNull
    @ApiModelProperty(required = true, notes = "Isbn of the book", example = "8993-3232-7628-X")
    private String isbn;

    @Setter
    @ManyToMany(mappedBy = "books")
    private List<User> users = Collections.emptyList();

    public void setGenre(String genre) {

        checkNotNull(genre, "Please check genre field, its null");

        this.genre = genre;

    }

    public void setAuthor(String author) {

        checkNotNull(author, "Please check author field, its null");

        this.author = author;

    }

    public void setImage(String image) {

        checkNotNull(image, "Please check image field, its null");

        this.image = image;

    }

    public void setTitle(String title) {

        checkNotNull(title, "Please check title field, its null");

        this.title = title;

    }

    public void setSubtitle(String subtitle) {

        checkNotNull(subtitle, "Please check subtitle field, its null");

        this.subtitle = subtitle;

    }

    public void setPublisher(String publisher) {

        checkNotNull(publisher, "Please check publisher field, its null");

        this.publisher = publisher;

    }

    public void setYear(String year) {

        checkNotNull(year, "Please check year field, its null");

        this.year = year;

    }

    public void setPages(int pages) {

        checkNotNull(pages, "Please check pages field, its null");
        checkArgument(pages > 20, "Please check pages field, its can't be 20 or less than 20");

        this.pages = pages;

    }

    public void setIsbn(String isbn) {

        checkNotNull(isbn, "Please check field isbn, its null");

        this.isbn = isbn;

    }

}
