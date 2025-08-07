package isang.orangeplanet.domain.goal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import isang.orangeplanet.domain.goal.controller.dto.WeeklyGoalAchievementDto;
import isang.orangeplanet.domain.goal.controller.request.CreateGoalRequest;
import isang.orangeplanet.domain.goal.controller.response.*;
import isang.orangeplanet.domain.goal.service.GoalService;
import isang.orangeplanet.domain.task.controller.response.ListTaskResponse;
import isang.orangeplanet.global.api_response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * GoalController : 목표 관련 Controller
 */
@RestController
@RequestMapping(value = "/goal")
@RequiredArgsConstructor
@Tag(name = "Goal", description = "목표 관련 API")
public class GoalController {
  private final GoalService goalService;

  /**
   * 목표 생성 엔드포인트
   * @param request : CreateGoalRequest 객체
   * @return : 생성된 목표 반환
   */
  @PostMapping(value = "/create")
  @Operation(summary = "목표 생성", description = "목표 생성 엔드포인트")
  public ApiResponse<GetGoalResponse> createGoal(@RequestBody CreateGoalRequest request) {
    return ApiResponse.onSuccess(this.goalService.createGoal(request));
  }

  /**
   * 특정 목표 조회 엔드포인트
   * @param goalId : 목표 ID
   * @return : 특정 목표 반환
   */
  @GetMapping(value = "/{id}")
  @Operation(summary = "특정 목표 조회", description = "특정 목표 조회 엔드포인트")
  public ApiResponse<GetGoalResponse> getGoal(@PathVariable("id") String goalId) {
    return ApiResponse.onSuccess(this.goalService.getGoal(goalId));
  }

  /**
   * 목표 목록 조회 엔드포인트
   * @return : 목표 목록 반환
   */
  @GetMapping(value = "/list")
  @Operation(summary = "목표 목록 조회", description = "목표 목록 조회 엔드포인트")
  public ApiResponse<ListGoalResponse> goalList() {
    return ApiResponse.onSuccess(this.goalService.goalList());
  }

  /**
   * 목표별 점수 조회 엔드포인트
   * @return : 목표별 점수 목록 반환
   */
  @GetMapping(value = "/score")
  @Operation(summary = "목표별 점수 목록 조회", description = "목표별 점수 목록 조회 엔드포인트")
  public ApiResponse<ListGoalScoresResponse> goalScoresList() {
    return ApiResponse.onSuccess(this.goalService.goalScoresList());
  }

  /**
   * 목표별 할일 목록 조회 엔드포인트
   * @param goalId : 목표 ID
   * @return : 목표별 할일 목록 반환
   */
  @GetMapping(value = "/task/list/{id}")
  @Operation(summary = "목표별 할일 목록 조회", description = "목표별 할일 목록 조회 엔드포인트")
  public ApiResponse<ListTaskResponse> goalTaskList(@PathVariable("id") String goalId) {
    return ApiResponse.onSuccess(this.goalService.goalTaskList(goalId));
  }

  /**
   * 주간 성과 상세 조회
   * @return 주간 성과 상세 목록 반환
   */
  @GetMapping(value = "/weekly/achievement")
  @Operation(summary = "주간 성과 조회", description = "주간 성과 조회 엔드포인트")
  public ApiResponse<List<WeeklyGoalAchievementDto>> weeklyAchievement() {
    return ApiResponse.onSuccess(this.goalService.weeklyAchievement());
  }

  /**
   * 목표별 달성률 조회
   * @return 목표별 달성률 반환
   */
  @GetMapping(value = "/progress")
  @Operation(summary = "목표별 달성률 조회", description = "목표별 달성률 조회 엔드포인트")
  public ApiResponse<ListGoalProgressResponse> getAchievementRateByGoal() {
    return ApiResponse.onSuccess(this.goalService.getAchievementRateByGoal());
  }

  /**
   * 특정 목표 삭제 엔드포인트
   * @param goalId : 목표 ID
   * @return : 공통 응답 객체 반환
   */
  @DeleteMapping(value = "/delete/{id}")
  @Operation(summary = "특정 목표 삭제", description = "특정 목표 삭제 엔드포인트")
  public ApiResponse<Void> deleteGoal(@PathVariable("id") String goalId) {
    this.goalService.deleteGoal(goalId);
    return ApiResponse.onSuccess();
  }
}
