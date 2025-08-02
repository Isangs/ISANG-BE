package isang.orangeplanet.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import isang.orangeplanet.auth.KakaoAPIClient;
import isang.orangeplanet.auth.KakaoClient;
import isang.orangeplanet.auth.dto.KakaoUserDto;
import isang.orangeplanet.global.api_response.exception.GeneralException;
import isang.orangeplanet.global.api_response.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final KakaoClient kakaoClient;
  private final KakaoAPIClient kakaoAPIClient;

  @Value("${oauth2.client-id}")
  private String clientId;

  @Value("${oauth2.client-secret}")
  private String clientSecretKey;

  @Value("${oauth2.redirect-uri}")
  private String redirectUri;

  public String kakaoLoginUrl() {
    return "https://kauth.kakao.com/oauth/authorize?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&response_type=code";
  }

  public void kakaoOAuth2Login(String code) {
    String accessToken = this.kakaoTokenRequest(code);
    KakaoUserDto kakaoUser = this.getKakaoUserInfo(accessToken);
    System.out.println(kakaoUser.toString());

    /*
      회원 저장 로직 구현
     */
  }

  private String kakaoTokenRequest(String code) {
    MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
    formParams.add("grant_type", "authorization_code");
    formParams.add("client_id", clientId);
    formParams.add("redirect_uri", redirectUri);
    formParams.add("code", code);
    formParams.add("client_secret", clientSecretKey);

    JsonNode tokenJson = kakaoClient.tokenRequest(formParams);
    return tokenJson.get("access_token").asText();
  }

  private KakaoUserDto getKakaoUserInfo(String accessToken) {
    JsonNode userInfoJson = kakaoAPIClient.userInfo("Bearer " + accessToken);

    try {
      return KakaoUserDto.builder()
        .kakaoUserId(
          userInfoJson.get("id").asText()
        ).nickName(
          userInfoJson.get("kakao_account")
            .get("profile")
            .get("nickname")
            .asText()
        ).profileUrl(
          userInfoJson.get("kakao_account")
            .get("profile")
            .get("profile_image_url")
            .asText()
        ).email(
          userInfoJson.get("kakao_account")
            .get("email")
            .asText()
        ).build();
    } catch (Exception e) {
      throw new GeneralException(ErrorStatus.INTERNAL_ERROR, "문제가 발생했습니다.");
    }
  }
}
