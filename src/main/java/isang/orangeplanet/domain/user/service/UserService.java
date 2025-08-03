package isang.orangeplanet.domain.user.service;

import isang.orangeplanet.domain.auth.utils.SecurityUtils;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.domain.user.controller.response.DetailUserResponse;
import isang.orangeplanet.domain.user.repository.JpaUserRepository;
import isang.orangeplanet.domain.user.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final JpaUserRepository jpaUserRepository;

  public DetailUserResponse getDetailUser() {
    User user = UserUtils.getUser(SecurityUtils.getAuthUserId());
    return DetailUserResponse.builder()
      .userId(user.getUserId())
      .name(user.getName())
      .nickName(user.getNickName())
      .email(user.getEmail())
      .profileUrl(user.getProfileUrl())
      .role(user.getRole())
      .introduce(user.getIntroduce())
      .level(user.getLevel())
      .totalScore(user.getTotalScore())
      .build();
  }
}
