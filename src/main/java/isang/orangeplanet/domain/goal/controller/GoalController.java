package isang.orangeplanet.domain.goal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import isang.orangeplanet.domain.goal.controller.request.CreateGoalRequest;
import isang.orangeplanet.domain.goal.controller.response.GetGoalResponse;
import isang.orangeplanet.domain.goal.controller.response.ListGoalResponse;
import isang.orangeplanet.domain.goal.service.GoalService;
import isang.orangeplanet.global.api_response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/goal")
@RequiredArgsConstructor
@Tag(name = "Goal", description = "목표 관련 API")
public class GoalController {
  private final GoalService goalService;

  @PostMapping(value = "/create")
  @Operation(summary = "목표 생성", description = "목표 생성 엔드포인트")
  public ApiResponse<GetGoalResponse> createGoal(@RequestBody CreateGoalRequest request) {
    return ApiResponse.onSuccess(this.goalService.createGoal(request));
  }

  @GetMapping(value = "/{id}")
  @Operation(summary = "특정 목표 조회", description = "특정 목표 조회 엔드포인트")
  public ApiResponse<GetGoalResponse> getGoal(@PathVariable("id") String goalId) {
    return ApiResponse.onSuccess(this.goalService.getGoal(goalId));
  }

  @GetMapping(value = "/list")
  @Operation(summary = "목표 목록 조회", description = "목표 목록 조회 엔드포인트")
  public ApiResponse<ListGoalResponse> goalList() {
    return ApiResponse.onSuccess(this.goalService.goalList());
  }

  @DeleteMapping(value = "/delete/{id}")
  @Operation(summary = "특정 목표 삭제", description = "특정 목표 삭제 엔드포인트")
  public ApiResponse<Void> deleteGoal(@PathVariable("id") String goalId) {
    this.goalService.deleteGoal(goalId);
    return ApiResponse.onSuccess();
  }
}
