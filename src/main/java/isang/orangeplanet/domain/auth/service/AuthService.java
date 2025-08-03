package isang.orangeplanet.domain.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import isang.orangeplanet.domain.auth.KakaoAPIClient;
import isang.orangeplanet.domain.auth.KakaoClient;
import isang.orangeplanet.domain.auth.controller.response.GetAuthInfoResponse;
import isang.orangeplanet.domain.auth.controller.response.GetTokenResponse;
import isang.orangeplanet.domain.auth.dto.JwtClaimsDto;
import isang.orangeplanet.domain.auth.dto.JwtDto;
import isang.orangeplanet.domain.auth.dto.KakaoUserDto;
import isang.orangeplanet.domain.auth.utils.JwtUtils;
import isang.orangeplanet.domain.auth.utils.RedisUtils;
import isang.orangeplanet.domain.auth.utils.SecurityUtils;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.domain.user.repository.JpaUserRepository;
import isang.orangeplanet.global.api_response.exception.GeneralException;
import isang.orangeplanet.global.api_response.status.ErrorStatus;
import isang.orangeplanet.global.utils.enums.Role;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * AuthService : 인증 관련 Service
 */
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
  private final JpaUserRepository userRepository;

  /**
   * Access Token 재발급 메서드
   * @param request : HttpServletRequest 객체
   * @return : GetTokenResponse 응답 객체 반환
   */
  public GetTokenResponse recreateAccessToken(HttpServletRequest request) {
    String refreshHeader = request.getHeader("Refresh-Token");
    if (refreshHeader != null && refreshHeader.startsWith("Bearer ")) {
      String refreshToken = refreshHeader.substring(7);

      // 해당 토큰의 유효 시간과 Redis에 있는 토큰과 동일한지 체크
      if (JwtUtils.getValidateToken(refreshToken) && refreshToken.equals(RedisUtils.get(refreshToken))) {
        String userId = JwtUtils.getUserId(refreshToken);

        JwtDto jwtDto = JwtUtils.createToken(
          JwtClaimsDto.builder()
            .userId(userId)
            .role(Role.USER)
            .build()
        );

        return GetTokenResponse.builder()
          .accessToken(jwtDto.getAccessToken())
          .build();
      } else {
        throw new GeneralException(ErrorStatus.TOKEN_EXPIRED, "토큰이 만료되었습니다.");
      }
    } else {
      throw new GeneralException(ErrorStatus.UNAUTHORIZED, "접근할 수 있는 권한이 없습니다.");
    }
  }

  /**
   * 로그아웃 (Redis에 저장된 Refresh Token 삭제)
   */
  public void logout() {
    String userId = SecurityUtils.getAuthUserId();
    RedisUtils.delete(userId);
  }

  /**
   * 카카오 로그인 화면 리다이렉션
   * @return : 리다이렉션 URL 반환
   */
  public String kakaoLoginUrl() {
    return "https://kauth.kakao.com/oauth/authorize?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&response_type=code";
  }

  /**
   * 로그인 메서드
   * @param code : 인가 코드
   * @return : GetAuthInfoResponse(로그인) 응답 객체 반환
   */
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
          .introduce("")
          .level(0L)
          .totalScore(0L)
          .build()
      );
    }

    return GetAuthInfoResponse.builder()
      .accessToken(jwtDto.getAccessToken())
      .refreshToken(jwtDto.getRefreshToken())
      .role(Role.USER.name())
      .build();
  }

  /**
   * Kakao Access Token 얻는 메서드
   * @param code : 인가 코드
   * @return : Kakao Access Token 반환
   */
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

  /**
   * Kakao Access Token을 사용해서 사용자 정보 얻는 메서드
   * @param accessToken : Access Token
   * @return : KakaoUserDto 객체 반환
   */
  private KakaoUserDto getKakaoUserInfo(String accessToken) {
    try {
      JsonNode userInfoJson = kakaoAPIClient.userInfo("Bearer " + accessToken);
      return KakaoUserDto.builder()
        .kakaoUserId(userInfoJson.get("id").asText())
        .nickName(
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
