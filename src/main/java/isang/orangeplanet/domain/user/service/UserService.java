package isang.orangeplanet.domain.user.service;

import isang.orangeplanet.domain.auth.utils.SecurityUtils;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.domain.user.controller.request.UpdateUserRequest;
import isang.orangeplanet.domain.user.controller.response.DetailUserResponse;
import isang.orangeplanet.domain.user.repository.UpdateUserRepository;
import isang.orangeplanet.domain.user.utils.UserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
  private final UpdateUserRepository updateUserRepository;

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

  public void updateUser(UpdateUserRequest request) {
    this.updateUserRepository.update(request);
  }
}
