package isang.orangeplanet.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * FeignClientConfig : 외부 API 호출 설정 파일
 */
@Configuration
public class FeignClientConfig {

  @Bean
  Request.Options requestOptions() {
    return new Request.Options(15, TimeUnit.SECONDS, 15, TimeUnit.SECONDS, false);
  }

  @Bean
  Retryer retry() {
    return new Retryer.Default(500, 1000, 2);
  }

  @Bean
  System.Logger.Level feignLoggerLevel() {
    return System.Logger.Level.ALL;
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}
