package wolox.training.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationProvider authenticationProvider;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(AuthenticationProvider authenticationProvider,
        PasswordEncoder passwordEncoder) {

        this.authenticationProvider = authenticationProvider;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/books", "/api/users").permitAll()
                .anyRequest().authenticated()
            .and().httpBasic()
            .and().csrf().disable();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(authenticationProvider)
            .passwordEncoder(passwordEncoder);

    }

}
