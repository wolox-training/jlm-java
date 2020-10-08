package wolox.training.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("wolox.training.repositories")
@EntityScan("wolox.training.models")
public class PersistenceConfig {

}
