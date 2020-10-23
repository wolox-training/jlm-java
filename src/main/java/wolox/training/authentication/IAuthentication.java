package wolox.training.authentication;

import org.springframework.security.core.Authentication;

public interface IAuthentication {

    /**
     * Method that gets authenticated user
     *
     * @return authenticated principal representation
     */
    Authentication getAuthentication();

}
