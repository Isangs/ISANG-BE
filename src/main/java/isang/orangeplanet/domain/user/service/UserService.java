package isang.orangeplanet.domain.user.service;

import isang.orangeplanet.domain.auth.utils.SecurityUtils;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.domain.user.controller.dto.request.UpdateUserRequest;
import isang.orangeplanet.domain.user.controller.dto.response.DetailUserResponse;
import isang.orangeplanet.domain.user.repository.UserRepository;
import isang.orangeplanet.domain.user.utils.UserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * UserService : 회원 관련 Service
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
  private final UserRepository updateUserRepository;

  /**
   * 회원 상세 정보 조회
   * @return : 회원 정보 반환
   */
  public DetailUserResponse getDetailUser() {
    User user = UserUtils.getUser(SecurityUtils.getAuthUserId());
    Long level = UserUtils.getLevel(user.getTotalScore());

    return DetailUserResponse.builder()
      .userId(user.getUserId())
      .name(user.getName())
      .nickName(user.getNickName())
      .email(user.getEmail())
      .profileUrl(user.getProfileUrl())
      .role(user.getRole())
      .introduce(user.getIntroduce())
      .level(level)
      .totalScore(user.getTotalScore())
      .build();
  }

  /**
   * 회원 정보 수정
   * @param request : UpdateUserRequest 객체
   */
  public void updateUser(UpdateUserRequest request) {
    this.updateUserRepository.update(request, SecurityUtils.getAuthUserId());
  }
}
