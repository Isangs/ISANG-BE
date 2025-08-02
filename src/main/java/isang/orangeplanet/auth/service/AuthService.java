package isang.orangeplanet.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  @Value("${oauth2.client-id}")
  private String clientId;

  @Value("${oauth2.client-secret}")
  private String clientSecretKey;

  @Value("${oauth2.redirect-uri}")
  private String redirectUri;

  public String kakaoLoginUrl() {
    return "https://kauth.kakao.com/oauth/authorize?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&response_type=code";
  }
}
