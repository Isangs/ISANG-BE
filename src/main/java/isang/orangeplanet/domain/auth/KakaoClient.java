package isang.orangeplanet.domain.auth;

import com.fasterxml.jackson.databind.JsonNode;
import isang.orangeplanet.global.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * KakaoClient
 */
@FeignClient(
  name = "kakao-client",
  url = "https://kauth.kakao.com",
  configuration = FeignClientConfig.class
)
public interface KakaoClient {

  /**
   * Access Token 발급 받는 API 호출
   * @param formParams : 토큰 발급에 필요한 파라미터들
   * @return : 토큰 JSON 반환
   */
  @PostMapping(value = "/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  JsonNode tokenRequest(MultiValueMap<String, String> formParams);
}
