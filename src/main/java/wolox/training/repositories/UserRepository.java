package wolox.training.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wolox.training.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Method that gets user by his username
     *
     * @param username: User's username (String)
     * @return a {@link User}
     */
    Optional<User> findByUsername(String username);

}
