package isang.orangeplanet.domain.auth.utils;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * RedisUtils : Redis 관련 서비스를 모아둔 유틸 클래스
 */
@Component
@RequiredArgsConstructor
public class RedisUtils {

  private final RedisTemplate<String, Object> nonRedisTemplate;
  private static RedisTemplate<String, Object> redisTemplate;

  @PostConstruct
  public void init() {
    redisTemplate = this.nonRedisTemplate;
  }

  /**
   * Redis에 Refresh Token을 저장하는 메서드
   * @param token : Access Token
   */
  public static void save(String token) {
    String userId = JwtUtils.getUserId(token);
    redisTemplate.opsForValue().set("rf-" + userId, token, Duration.ofSeconds(604800));
  }

  /**
   * Redis에서 Refresh Token을 얻는 메서드
   * @param token : Access Token
   * @return : Refresh Token 반환
   */
  public static String get(String token) {
    String userId = JwtUtils.getUserId(token);
    return (String) redisTemplate.opsForValue().get("rf-" + userId);
  }

  /**
   * Refresh Token을 Redis에서 제거하는 메서드
   * @param userId : 인증된 회원 ID
   */
  public static void delete(String userId) {
    redisTemplate.delete("rf-" + userId);
  }
}
