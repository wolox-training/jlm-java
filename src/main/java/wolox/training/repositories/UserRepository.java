package wolox.training.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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

    /**
     * Method that obtain all users if your birthdate be between two date
     *
     * @param maxDate:        Max date ({@link LocalDate})
     * @param minDate:        Min date ({@link LocalDate})
     * @param nameContaining: Name containing of the user (String)
     * @return {@link User} list
     */
    @Query("SELECT u FROM User u WHERE u.birthdate <= :maxDate and u.birthdate >= :minDate and (LOWER(u.name) like LOWER(CONCAT('%', :nameContaining, '%')))")
    List<User> findByBirthdateBetweenTwoDateCustom(
        @Param("maxDate") LocalDate maxDate, @Param("minDate") LocalDate minDate,
        @Param("nameContaining") String nameContaining);

    /**
     * Method that obtain all users if your birthdate be between two date
     *
     * @param maxDate:        Max date ({@link LocalDate})
     * @param minDate:        Min date ({@link LocalDate})
     * @param nameContaining: Name containing of the user (String)
     * @return {@link User} list
     */
    List<User> findByBirthdateLessThanEqualAndBirthdateGreaterThanEqualAndNameContainingIgnoreCase(
        LocalDate maxDate, LocalDate minDate, String nameContaining);

}
