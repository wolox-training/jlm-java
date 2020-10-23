package wolox.training.client.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BooksParam {

    private String bibkeys;
    private String format = "json";
    private String jscmd = "data";

    public BooksParam(String bibkeys) {

        this.bibkeys = bibkeys;

    }

}
