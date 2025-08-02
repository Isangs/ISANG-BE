package isang.orangeplanet.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import isang.orangeplanet.auth.KakaoAPIClient;
import isang.orangeplanet.auth.KakaoClient;
import isang.orangeplanet.auth.controller.response.GetAuthInfoResponse;
import isang.orangeplanet.auth.dto.JwtClaimsDto;
import isang.orangeplanet.auth.dto.JwtDto;
import isang.orangeplanet.auth.dto.KakaoUserDto;
import isang.orangeplanet.auth.utils.JwtUtils;
import isang.orangeplanet.auth.utils.RedisUtils;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.domain.user.repository.UserRepository;
import isang.orangeplanet.global.api_response.exception.GeneralException;
import isang.orangeplanet.global.api_response.status.ErrorStatus;
import isang.orangeplanet.global.utils.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@RequiredArgsConstructor
public class AuthService {

  @Value("${oauth2.client-id}")
  private String clientId;

  @Value("${oauth2.client-secret}")
  private String clientSecretKey;

  @Value("${oauth2.redirect-uri}")
  private String redirectUri;

  private final KakaoClient kakaoClient;
  private final KakaoAPIClient kakaoAPIClient;
  private final UserRepository userRepository;

  public String kakaoLoginUrl() {
    return "https://kauth.kakao.com/oauth/authorize?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&response_type=code";
  }

  public GetAuthInfoResponse kakaoOAuth2Login(String code) {
    String accessToken = this.kakaoTokenRequest(code);
    KakaoUserDto kakaoUser = this.getKakaoUserInfo(accessToken);

    JwtDto jwtDto = JwtUtils.createToken(
      JwtClaimsDto.builder()
        .userId(kakaoUser.getKakaoUserId())
        .role(Role.USER)
        .build()
    );

    // Redis에 Refresh Token 저장
    RedisUtils.save(jwtDto.getRefreshToken());

    User user = this.userRepository.findById(kakaoUser.getKakaoUserId()).orElse(null);

    // 회원 정보 저장
    if (user == null) {
      this.userRepository.save(
        User.builder()
          .userId(kakaoUser.getKakaoUserId())
          .name(kakaoUser.getNickName())
          .nickName(kakaoUser.getNickName())
          .profileUrl(kakaoUser.getProfileUrl())
          .email(kakaoUser.getEmail())
          .role(Role.USER)
          .build()
      );
    }

    return GetAuthInfoResponse.builder()
      .accessToken(jwtDto.getAccessToken())
      .refreshToken(jwtDto.getRefreshToken())
      .role(Role.USER.name())
      .build();
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
