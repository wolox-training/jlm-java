package wolox.training.client.feign;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.HashMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import wolox.training.client.models.BooksParam;

@FeignClient(
    name = "${feign.openLibrary.name}", url = "${feign.openLibrary.rootUrl}"
)
public interface OpenLibraryFeignClient {

    /**
     * Method that obtain a book by your isbn
     *
     * @return
     */
    @GetMapping(
        value = "${feign.openLibrary.resources.books}", produces = APPLICATION_JSON_VALUE
    )
    HashMap<String, Object> findBookByIsbn(@SpringQueryMap BooksParam parameters);

}
