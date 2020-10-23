package wolox.training.client.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BookInfoDto {

    private String isbn;
    private String title;
    private String subtitle;
    private String publishDate;
    private int numberOfPages;
    private List<PublisherDto> publishers;
    private CoverDto cover;
    private List<AuthorDto> authors;

}
