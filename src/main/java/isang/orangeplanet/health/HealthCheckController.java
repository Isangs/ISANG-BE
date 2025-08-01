package isang.orangeplanet.health;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HealthController : AWS Elastic Beanstalk Health Check를 위한 Controller
 */

@Hidden
@RestController
public class HealthCheckController {

  /**
   * Health Check를 위한 엔드포인트
   * @return : OK 반환
   */
  @GetMapping(value = "/health")
  public String healthCheck() {
    return "OK";
  }
}
