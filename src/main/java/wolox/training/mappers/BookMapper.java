package wolox.training.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import wolox.training.client.models.BookInfoDto;
import wolox.training.models.Book;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookMapper {

    /**
     * Method that convert bookInfoDto to Book entity
     *
     * @param isbn:        Isbn of the book (String)
     * @param bookInfoDto: OpenLibrary response ({@link BookInfoDto})
     * @return {@link Book}
     */
    @Mapping(target = "isbn", source = "isbn")
    @Mapping(target = "subtitle", source = "bookInfoDto.title")
    @Mapping(target = "year", source = "bookInfoDto.publishDate")
    @Mapping(target = "pages", source = "bookInfoDto.numberOfPages")
    @Mapping(target = "image", source = "bookInfoDto.cover.small")
    Book bookInfoDtoToToEntity(String isbn, BookInfoDto bookInfoDto);

    @AfterMapping
    default void setAuthorAndPublisher(@MappingTarget Book book, BookInfoDto bookInfoDto) {

        if (!bookInfoDto.getAuthors().isEmpty()) {

            book.setAuthor(bookInfoDto.getAuthors().get(0).getName());

        }

        if (!bookInfoDto.getPublishers().isEmpty()) {

            book.setPublisher(bookInfoDto.getPublishers().get(0).getName());

        }

    }

}
