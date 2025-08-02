package isang.orangeplanet.auth;

import com.fasterxml.jackson.databind.JsonNode;
import isang.orangeplanet.global.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
  name = "kakao-client",
  url = "https://kauth.kakao.com",
  configuration = FeignClientConfig.class
)
public interface KakaoClient {

  @PostMapping(value = "/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  JsonNode tokenRequest(MultiValueMap<String, String> formParams);
}
