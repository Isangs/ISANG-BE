package isang.orangeplanet.domain.goal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import isang.orangeplanet.domain.goal.controller.request.CreateGoalRequest;
import isang.orangeplanet.domain.goal.controller.response.GetGoalResponse;
import isang.orangeplanet.domain.goal.service.GoalService;
import isang.orangeplanet.global.api_response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/goal")
@RequiredArgsConstructor
@Tag(name = "Goal", description = "목표 관련 API")
public class GoalController {
  private final GoalService goalService;

  @PostMapping(value = "/create")
  @Operation(summary = "목표 생성", description = "목표 생성 엔드포인트")
  public ApiResponse<GetGoalResponse> createGoal(@RequestBody CreateGoalRequest request) {
    return ApiResponse.onSuccess(goalService.createGoal(request));
  }
}
