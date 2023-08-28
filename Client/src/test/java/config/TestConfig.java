package config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("file:src/main/webapp/WEB-INF/Spring-servlet.xml")
public class TestConfig {

}
