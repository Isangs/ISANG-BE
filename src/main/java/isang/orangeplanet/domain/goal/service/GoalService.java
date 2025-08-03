package isang.orangeplanet.domain.goal.service;

import isang.orangeplanet.domain.auth.utils.SecurityUtils;
import isang.orangeplanet.domain.goal.Goal;
import isang.orangeplanet.domain.goal.controller.request.CreateGoalRequest;
import isang.orangeplanet.domain.goal.controller.response.GetGoalResponse;
import isang.orangeplanet.domain.goal.repository.JpaGoalRepository;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.domain.user.utils.UserUtils;
import isang.orangeplanet.global.api_response.exception.GeneralException;
import isang.orangeplanet.global.api_response.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoalService {
  private final JpaGoalRepository jpaGoalRepository;

  public GetGoalResponse createGoal(CreateGoalRequest request) {
    User user = UserUtils.getUser(SecurityUtils.getAuthUserId());

    this.jpaGoalRepository.save(
      Goal.builder()
        .name(request.name())
        .colorCode(request.colorCode())
        .user(user)
        .build()
    );

    Goal goal = this.jpaGoalRepository.findGoalByName(request.name());

    return GetGoalResponse.builder()
      .goalId(goal.getGoalId())
      .name(goal.getName())
      .colorCode(goal.getColorCode())
      .build();
  }

  public GetGoalResponse getGoal(String goalId) {
    Goal goal = this.jpaGoalRepository.findById(Long.parseLong(goalId))
      .orElseThrow(() -> new GeneralException(ErrorStatus.KEY_NOT_EXIST, "목표를 찾을 수 없습니다."));

    return GetGoalResponse.builder()
      .goalId(goal.getGoalId())
      .name(goal.getName())
      .colorCode(goal.getColorCode())
      .build();
  }
}
