package isang.orangeplanet.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CorsConfig : Cors 관련 설정 파일
 */
@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
      .allowedOriginPatterns(
        "http://localhost:3000",
        "https://prod-api.isang.site",
        "https://api.isang.site/"
      )
      .allowedMethods("*")
      .allowedHeaders("*")
      .allowCredentials(true)
      .exposedHeaders("Authorization")
      .maxAge(3600);
  }
}
