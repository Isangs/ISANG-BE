package isang.orangeplanet.domain.user.utils;

import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.domain.user.repository.JpaUserRepository;
import isang.orangeplanet.global.api_response.exception.GeneralException;
import isang.orangeplanet.global.api_response.status.ErrorStatus;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * UserUtils : 회원 관련 유틸 클래스
 */
@Component
@RequiredArgsConstructor
public class UserUtils {

  private final JpaUserRepository nonJpaUserRepository;
  private static JpaUserRepository jpaUserRepository;

  @PostConstruct
  public void init() {
    jpaUserRepository = this.nonJpaUserRepository;
  }

  /**
   * 인증된 회원 검색 메서드
   * @param userId : 회원 ID
   * @return : 회원 반환
   */
  public static User getUser(String userId) {
    return jpaUserRepository.findById(userId)
      .orElseThrow(() -> new GeneralException(ErrorStatus.KEY_NOT_EXIST, "회원을 찾을 수 없습니다."));
  }

  public static Long getLevel(Long totalScore) {
    long taskCount = Math.max(1, totalScore / 100);
    return Math.round(Math.pow(taskCount, 0.7));
  }
}