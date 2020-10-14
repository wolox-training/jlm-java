package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PRECONDITION_FAILED, reason = "User's verification required")
public class UserPreconditionFailedException extends RuntimeException {

    public UserPreconditionFailedException() {
        super();
    }

}
