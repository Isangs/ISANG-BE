package isang.orangeplanet.domain.task.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import isang.orangeplanet.domain.auth.utils.SecurityUtils;
import isang.orangeplanet.domain.goal.Goal;
import isang.orangeplanet.domain.goal.controller.response.GetGoalResponse;
import isang.orangeplanet.domain.goal.repository.JpaGoalRepository;
import isang.orangeplanet.domain.task.Task;
import isang.orangeplanet.domain.task.controller.request.CreateTaskRequest;
import isang.orangeplanet.domain.task.controller.request.UpdateTaskRequest;
import isang.orangeplanet.domain.task.controller.response.FetchTaskVisibilityResponse;
import isang.orangeplanet.domain.task.controller.response.ListTaskDto;
import isang.orangeplanet.domain.task.controller.response.ListTaskResponse;
import isang.orangeplanet.domain.task.repository.JpaTaskRepository;
import isang.orangeplanet.domain.task.repository.TaskRepository;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.domain.user.utils.UserUtils;
import isang.orangeplanet.global.api_response.exception.GeneralException;
import isang.orangeplanet.global.api_response.status.ErrorStatus;
import isang.orangeplanet.global.utils.enums.Priority;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TaskService : 할일 관련 Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TaskService {

  private final JpaGoalRepository jpaGoalRepository;
  private final JpaTaskRepository jpaTaskRepository;
  private final TaskRepository taskRepository;
  private final ObjectMapper objectMapper;
  private final ChatClient chatClient;

  public FetchTaskVisibilityResponse getTaskById(Long id) {
    User user = UserUtils.getUser(SecurityUtils.getAuthUserId());
    Task task = jpaTaskRepository.findById(id).orElseThrow(() ->
        new GeneralException(ErrorStatus.NOT_FOUND, "해당하는 할일을 찾을 수 없습니다.")
    );

    if(task.getUser() != user) {
      throw new GeneralException(ErrorStatus.BAD_REQUEST, "자신의 할일만 조회할 수 있습니다.");
    }

    return FetchTaskVisibilityResponse.builder()
        .isAddFeed(task.getIsAddFeed())
        .isPublic(task.getIsPublic())
        .build();
  }

  public void updateTask(Long id, UpdateTaskRequest request) {
    User user = UserUtils.getUser(SecurityUtils.getAuthUserId());
    Task task = jpaTaskRepository.findById(id).orElseThrow(() ->
        new GeneralException(ErrorStatus.NOT_FOUND, "해당하는 할일을 찾을 수 없습니다.")
    );

    if(task.getUser() != user) {
       throw new GeneralException(ErrorStatus.BAD_REQUEST, "자신의 할일만 수정할 수 있습니다.");
    }

    task.updateVisibility(
        request.isAddRecord(),
        request.isPublic()
    );
  }

  /**
   * 할일 추가(생성) 메서드 (Spring AI 사용)
   * @param request : CreateTaskRequest 객체 (할일 추가 요청 객체)
   */
  public void createTask(CreateTaskRequest request) {
    User user = UserUtils.getUser(SecurityUtils.getAuthUserId());

    Goal goal;
    int percentage;

    /*
      우리는 목표와 우선순위중 하나라도 request를 통해 받지 않았을때 그 하나를 AI가 자동으로 선정할 수 있게 로직을 설계해야 합니다.
      만약 둘다 받지 않았으면 둘다 AI 사용해야죠?
     */

    if (request.goalId().isEmpty()) { // 목표 ID가 비어있을때 (선택하지 않았을때)
      // 해당 회원의 모든 목표를 조회함.
      List<Goal> goalList = this.jpaGoalRepository.findGoalByUser(user)
        .orElseThrow(() -> new GeneralException(ErrorStatus.KEY_NOT_EXIST, "목표 목록을 찾을 수 없습니다."));
      goalList.forEach(g -> System.out.println(g.getName()));

      // 프롬프트에 추가될 목표 목록 제작
      StringBuffer goals = new StringBuffer("[");
      goalList.forEach(g -> goals.append(g.getName()).append(", "));
      goals.append("]");

      // 실제 AI한테 날릴 프롬프트
      String prompt;
      if (goalList.isEmpty()) {
        prompt = request.name() + "는 어느 유형(goal)에 속하는지 **반드시 백틱 없이 {\"goal\": \"값\"} JSON 문자열만 반환해줘** 목표는 반드시 하나의 단어로 간결하게 작성하고 어색한 단어 없이 새로운 **하나의 유형**을 만들어서 JSON으로 반환해줘";
      } else {
        prompt = request.name() + "는 " + goals + " 중에서 어느 유형(goal)에 속하는지 반드시 백틱 없이 **{\"goal\": \"값\"} JSON 형식으로 반환해줘** 만약 다음 리스트에서 유사한 유형이 없다면 기존 리스트를 참고해서 어색한 단어 없이 새로운 하나의 유형을 만들어서 JSON으로 반환해줘";
      }

      try {
        // AI 답변을 JSON으로 변환
        String response = question(prompt);
        JsonNode json = this.objectMapper.readTree(response);

        // 목표 추출
        String goalName = json.get("goal").asText();

        // 그 목표 찾기
        goal = this.jpaGoalRepository.findGoalByName(goalName);

        // 찾은 목표가 DB에 없다면 아래 실행
        if (goalList.isEmpty() || goal == null) {
          this.jpaGoalRepository.save(
            Goal.builder()
              .name(goalName)
              .colorCode("#87CEEB")
              .user(user)
              .build()
          );
        }

        // AI가 준 목표 이름으로 목표를 조회
        goal = this.jpaGoalRepository.findGoalByName(goalName);

        log.info("ChatGPT가 선정한 목표 = {}", goalName);
      } catch (Exception e) {
        e.printStackTrace();
        throw new GeneralException(ErrorStatus.INTERNAL_ERROR, "ChatGPT API 호출중 문제가 발생했습니다.");
      }
    } else { // request에 목표 ID가 있다면 해당 목표를 찾아 goal에 넣어줌
      goal = this.jpaGoalRepository.findById(Long.parseLong(request.goalId()))
        .orElseThrow(() -> new GeneralException(ErrorStatus.KEY_NOT_EXIST, "목표를 찾을 수 없습니다."));
    }

    if (request.priority().isEmpty()) { // 우선순위가 비어있다면 AI 호출
      // 실제 AI한테 날릴 프롬프트
      String prompt = "사용자의" + request.name() + "와" + goal.getName() + "의 연관도를 0~100 사이 정수(percent)로 계산한다.\n" +
        "설명, 말머리, 코드블록, 여는/닫는 텍스트 없이 **JSON만** 출력한다.\n" + "출력 형식(반드시 준수):\n" + "{\"percent\": 50}";

      try {
        // AI 답변을 JSON으로 변환
        JsonNode json = this.objectMapper.readTree(question(prompt));

        // 백분율 추출
        percentage = json.get("percent").asInt();

        log.info("ChatGPT 산정 연관도(%) = {}%", percentage);
      } catch (Exception e) {
        e.printStackTrace();
        throw new GeneralException(ErrorStatus.INTERNAL_ERROR, "ChatGPT API 호출중 문제가 발생했습니다.");
      }
    } else { // request에 우선순위를 있다면 아래 실행
      percentage = Priority.valueOf(request.priority()).getScore();
    }

    // 최종적으로 Task에 저장
    this.jpaTaskRepository.save(
      Task.builder()
        .name(request.name())
        .isAddFeed(true)
        .isPublic(false)
        .priority(percentage)
        .deadline(request.deadline())
        .goal(goal)
        .user(user)
        .build()
    );
  }

  /**
   * 전체 할일 목록 조회 메서드
   * @return : 전체 할일 목록 반환
   */
  public ListTaskResponse getTaskList() {
    String userId = SecurityUtils.getAuthUserId();

    List<Task> tasks = this.taskRepository.findTaskByUserIdAndIsCompleted(userId, false);
    int maxScore = tasks.size() * 100;
    int percent = (int) Math.round(10000.0 / maxScore);

    List<ListTaskDto> responses = tasks.stream().map(task -> {
      GetGoalResponse goalResponse = GetGoalResponse.builder()
          .goalId(task.getGoal().getGoalId())
          .name(task.getGoal().getName())
          .colorCode(task.getGoal().getColorCode())
          .build();

      return ListTaskDto.builder()
          .taskId(task.getTaskId())
          .goal(goalResponse)
          .name(task.getName())
          .priority(this.enumPriority(task.getPriority()))
          .percentageScore(percent)
          .deadline(task.getDeadline())
          .build();
    }).toList();

    return new ListTaskResponse(responses);
  }

  /**
   * 특정 할일 삭제 메서드
   * @param taskId : 할일 ID
   */
  public void deleteTask(String taskId) {
    this.jpaTaskRepository.deleteById(Long.parseLong(taskId));
  }

  /**
   * 우선순위를 Enum 값으로 만들기
   * @param priority : 우선순위 점수(?)
   * @return : Priority Enum 반환
   */
  public Priority enumPriority(int priority) {
    if (priority >= 80) {
      return Priority.HIGH;
    } else if (priority >= 50) {
      return Priority.NORMAL;
    } else if (priority >= 20) {
      return Priority.LOW;
    } else {
      return Priority.NORMAL;
    }
  }

  /**
   * Spring AI를 사용한 AI한테 질문 날리는 메서드
   * @param prompt : 프롬프트
   * @return : AI의 응답
   */
  private String question(String prompt) {
    return chatClient.prompt()
      .user(prompt)
      .call()
      .content();
  }
}
