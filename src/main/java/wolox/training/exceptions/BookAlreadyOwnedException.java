package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PRECONDITION_FAILED, reason = "Repeated book")
public class BookAlreadyOwnedException extends RuntimeException {

    public BookAlreadyOwnedException() {
        super();
    }

}
