package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PRECONDITION_FAILED, reason = "Book's verification required")
public class BookPreconditionFailedException extends RuntimeException {

    public BookPreconditionFailedException() {
        super();
    }

}
