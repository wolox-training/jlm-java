package wolox.training.client.delegate;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Optional;
import org.springframework.stereotype.Component;
import wolox.training.client.feign.OpenLibraryFeignClient;
import wolox.training.client.models.BookInfoDto;
import wolox.training.client.models.BooksParam;

@Component
public class OpenLibraryDelegate {

    private static final String ISBN_PARAM = "ISBN:";

    private final OpenLibraryFeignClient openLibraryFeignClient;
    private final ObjectMapper objectMapper;

    public OpenLibraryDelegate(OpenLibraryFeignClient openLibraryFeignClient,
        ObjectMapper objectMapper) {

        this.openLibraryFeignClient = openLibraryFeignClient;
        this.objectMapper = objectMapper;

    }

    public Optional<BookInfoDto> findBookByIsbn(String isbn) {

        StringBuilder isbnParam = new StringBuilder();
        isbnParam.append(ISBN_PARAM);
        isbnParam.append(isbn);

        HashMap<String, Object> bookInfoResponse = openLibraryFeignClient
            .findBookByIsbn(new BooksParam(isbnParam.toString()));

        if (!bookInfoResponse.isEmpty()) {

            return Optional.ofNullable(objectMapper
                .convertValue(bookInfoResponse.get(isbnParam.toString()), BookInfoDto.class));

        }

        return Optional.empty();

    }
}
