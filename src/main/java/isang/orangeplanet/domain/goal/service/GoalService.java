package isang.orangeplanet.domain.goal.service;

import isang.orangeplanet.domain.auth.utils.SecurityUtils;
import isang.orangeplanet.domain.goal.Goal;
import isang.orangeplanet.domain.goal.controller.request.CreateGoalRequest;
import isang.orangeplanet.domain.goal.controller.response.*;
import isang.orangeplanet.domain.goal.repository.GoalRepository;
import isang.orangeplanet.domain.goal.repository.JpaGoalRepository;
import isang.orangeplanet.domain.task.Task;
import isang.orangeplanet.domain.task.controller.response.ListTaskDto;
import isang.orangeplanet.domain.task.controller.response.ListTaskResponse;
import isang.orangeplanet.domain.task.repository.TaskRepository;
import isang.orangeplanet.domain.task.service.TaskService;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.domain.user.utils.UserUtils;
import isang.orangeplanet.global.api_response.exception.GeneralException;
import isang.orangeplanet.global.api_response.status.ErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * GoalService : 목표 관련 Service
 */
@Service
@RequiredArgsConstructor
@Transactional
public class GoalService {

  private final List<String> allDays = List.of(
    "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"
  );

  private final JpaGoalRepository jpaGoalRepository;
  private final GoalRepository goalRepository;
  private final TaskRepository taskRepository;
  private final TaskService taskService;

  /**
   * 목표 생성 메서드
   * @param request : CreateGoalRequest 객체
   * @return : 생성된 목표 반환
   */
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

  /**
   * 특정 목표 조회 메서드
   * @param goalId : 목표 ID
   * @return : 특정 목표 반환
   */
  public GetGoalResponse getGoal(String goalId) {
    Goal goal = this.jpaGoalRepository.findById(Long.parseLong(goalId))
      .orElseThrow(() -> new GeneralException(ErrorStatus.KEY_NOT_EXIST, "목표를 찾을 수 없습니다."));

    return GetGoalResponse.builder()
      .goalId(goal.getGoalId())
      .name(goal.getName())
      .colorCode(goal.getColorCode())
      .build();
  }

  /**
   * 목표 목록 조회 메서드
   * @return : 목표 목록 반환
   */
  public ListGoalResponse goalList() {
    User user = UserUtils.getUser(SecurityUtils.getAuthUserId());
    List<GetGoalResponse> goalList = new ArrayList<>();

    List<Goal> goals = this.jpaGoalRepository.findGoalByUser(user)
      .orElseThrow(() -> new GeneralException(ErrorStatus.KEY_NOT_EXIST, "목록을 찾을 수 없습니다."));

    goals.forEach(goal -> {
      goalList.add(
        GetGoalResponse.builder()
          .goalId(goal.getGoalId())
          .name(goal.getName())
          .colorCode(goal.getColorCode())
          .build()
      );
    });

    return ListGoalResponse.builder()
      .goalList(goalList)
      .build();
  }

  /**
   * 목표별 점수 목록 조회 메서드
   * @return : 목표별 점수 목록 반환
   */
  public ListGoalScoresResponse goalScoresList() {
    String userId = SecurityUtils.getAuthUserId();

    List<GetGoalDto> dto = this.goalRepository.listDetailGoal(userId);

    List<ListGoalScoresDto> detailGoalList = dto.stream().map(g -> {
      int max = Math.toIntExact(Math.max(1, g.getMaxScore()));
      int percent = (int) Math.round(100.0 * g.getScore() / max);

      return ListGoalScoresDto.builder()
          .goalId(g.getGoalId())
          .name(g.getName())
          .score(g.getScore())
          .maxScore((int) g.getMaxScore())
          .colorCode(g.getColorCode())
          .percentage(percent)
          .build();
    }).toList();

    return new ListGoalScoresResponse(detailGoalList);
  }

  /**
   * 목표별 할일 목록 조회 메서드
   * @param goalId : 목표 ID
   * @return : 해당 목표의 할일 목록 반환
   */
  public ListTaskResponse goalTaskList(String goalId) {
    String userId = SecurityUtils.getAuthUserId();

    List<Task> tasks = this.taskRepository.taskListByGoalId(Long.parseLong(goalId), userId);
    int maxScore = tasks.size() * 100;
    int percent = (int) Math.round(10000.0 / maxScore);

    List<ListTaskDto> responses = tasks.stream().map(t -> {
      GetGoalResponse goalResponse = GetGoalResponse.builder()
          .goalId(t.getGoal().getGoalId())
          .name(t.getGoal().getName())
          .colorCode(t.getGoal().getColorCode())
          .build();

      return ListTaskDto.builder()
          .taskId(t.getTaskId())
          .goal(goalResponse)
          .name(t.getName())
          .priority(this.taskService.enumPriority(t.getPriority()))
          .percentageScore(percent)
          .deadline(t.getDeadline())
          .build();
    }).toList();

    return new ListTaskResponse(responses);
  }

  /**
   * 주간 성과 목록 조회 메서드
   * @return : 주간 성과 목록 반환
   */
  public List<WeeklyGoalAchievementDto> weeklyAchievement() {
    List<ListWeeklyAchievementDto> rawList = this.goalRepository.weeklyAchievement(SecurityUtils.getAuthUserId());
    return convertToWeeklyGoals(rawList);
  }

  /**
   * DayList 채워넣기(?) ㅎㅎ
   * @param rawList : 주간 완료된 할일 목록 객체
   * @return : WeeklyGoalAchievementDto 목록 반환
   */
  public List<WeeklyGoalAchievementDto> convertToWeeklyGoals(List<ListWeeklyAchievementDto> rawList) {
    Map<Long, WeeklyGoalAchievementDto> map = new LinkedHashMap<>();
    Map<Long, Map<String, Long>> scoreByGoalAndDay = new HashMap<>();

    for (ListWeeklyAchievementDto dto : rawList) {
      map.computeIfAbsent(dto.getGoalId(), id ->
        WeeklyGoalAchievementDto.builder()
          .goalId(dto.getGoalId())
          .name(dto.getName())
          .maxScore(dto.getMaxScore().intValue())
          .dayList(new ArrayList<>())
          .build()
      );

      // 요일별 점수 map으로 저장
      scoreByGoalAndDay
        .computeIfAbsent(dto.getGoalId(), k -> new HashMap<>())
        .put(dto.getDay().toUpperCase(), dto.getScore());
    }

    for (WeeklyGoalAchievementDto goal : map.values()) {
      Map<String, Long> dayMap = scoreByGoalAndDay.getOrDefault(goal.getGoalId(), Collections.emptyMap());
      int total = 0;

      for (String day : allDays) {
        Long score = dayMap.getOrDefault(day, 0L);
        goal.getDayList().add(
          DayScoreDto.builder()
            .day(day)
            .score(score)
            .build()
        );
        total += score;
      }

      goal.setTotalScore(total);
    }

    return new ArrayList<>(map.values());
  }

  /**
   * 특정 목표 삭제 메서드
   * @param goalId : 목표 ID
   */
  public void deleteGoal(String goalId) {
    this.jpaGoalRepository.deleteById(Long.parseLong(goalId));
  }
}
