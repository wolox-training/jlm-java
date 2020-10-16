package wolox.training.security;

import java.util.Collections;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import wolox.training.repositories.UserRepository;

@Component
public class AuthenticationProvider implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthenticationProvider(UserRepository userRepository) {

        this.userRepository = userRepository;

    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        wolox.training.models.User userFound = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new User(userFound.getUsername(), userFound.getPassword(), Collections.emptyList());

    }
}
