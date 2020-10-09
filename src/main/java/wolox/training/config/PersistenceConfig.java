package wolox.training.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan("wolox.training.models")
public class PersistenceConfig {

}
