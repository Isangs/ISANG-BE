package isang.orangeplanet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class IsangBeApplication {
  public static void main(String[] args) {
    SpringApplication.run(IsangBeApplication.class, args);
  }
}
