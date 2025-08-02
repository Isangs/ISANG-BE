package isang.orangeplanet.domain.auth;

import com.fasterxml.jackson.databind.JsonNode;
import isang.orangeplanet.global.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * KakaoAPIClient
 */
@FeignClient(
  name = "kakao-api-client",
  url = "https://kapi.kakao.com/v2",
  configuration = FeignClientConfig.class
)
public interface KakaoAPIClient {

  /**
   * 사용자 정보 얻는 API 호출
   * @param token : Kakao Access Token
   * @return : 사용자 정보 JSON 반환
   */
  @GetMapping(value = "/user/me", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  JsonNode userInfo(@RequestHeader(value = "Authorization") String token);
}
