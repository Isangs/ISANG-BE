package isang.orangeplanet.domain.auth.utils;

import isang.orangeplanet.global.api_response.exception.GeneralException;
import isang.orangeplanet.global.api_response.status.ErrorStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * SecurityUtils : 자주 사용하는 인증 관련 로직을 하나로 묶은 유틸 클래스
 */
public class SecurityUtils {

  /**
   * SecurityContextHolder에서 Authentication 객체 얻는 메서드
   * @return : Authentication 반환
   */
  private static Authentication getAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getName() == null) {
      throw new GeneralException(ErrorStatus.TOKEN_EXPIRED, "만료된 토큰입니다.");
    }

    return authentication;
  }

  /**
   * 인증된 회원 ID 얻는 메서드
   * @return : 회원 ID 반환
   */
  public static String getAuthUserId() {
    Authentication authentication = getAuthentication();
    return authentication.getName();
  }
}
