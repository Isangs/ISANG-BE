package isang.orangeplanet.domain.task.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import isang.orangeplanet.domain.auth.utils.SecurityUtils;
import isang.orangeplanet.domain.goal.Goal;
import isang.orangeplanet.domain.goal.repository.JpaGoalRepository;
import isang.orangeplanet.domain.task.Task;
import isang.orangeplanet.domain.task.controller.request.CreateTaskRequest;
import isang.orangeplanet.domain.task.repository.JpaTaskRepository;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.domain.user.utils.UserUtils;
import isang.orangeplanet.global.api_response.exception.GeneralException;
import isang.orangeplanet.global.api_response.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

  private final JpaGoalRepository jpaGoalRepository;
  private final JpaTaskRepository jpaTaskRepository;
  private final ChatClient chatClient;
  private final ObjectMapper objectMapper;

  public void createTask(CreateTaskRequest request) {
    User user = UserUtils.getUser(SecurityUtils.getAuthUserId());

    Goal goal = null;
    int percentage = 0;
    if (!request.goalId().isEmpty()) {
      goal = this.jpaGoalRepository.findById(Long.parseLong(request.goalId()))
        .orElseThrow(() -> new GeneralException(ErrorStatus.KEY_NOT_EXIST, "목표를 찾을 수 없습니다."));
    } else {
      List<Goal> goalList = this.jpaGoalRepository.findGoalByUser(user)
        .orElseThrow(() -> new GeneralException(ErrorStatus.KEY_NOT_EXIST, "목표 목록을 찾을 수 없습니다."));

      StringBuffer goals = new StringBuffer("(");
      goalList.forEach(g -> goals.append(g.getName()).append(", "));
      goals.append(")");

      /*
        목표랑 매칭하는 과정에서 얼마나 목표랑 할일이 관련이 있는지를 수치로 받게 되고 그 수치값을 오름차순으로 정렬!! (ㅇㅋ?)
      */

      String prompt = request.name() + "는" + goals + "중에서 어떤 목표에 적합할지." +
        "그리고 " + request.name() + " 할일과 너가 정해준 goal과 얼마나 연관있는지 백분율로 구해주고 {\"goal\":\"업무\", \"percent\":\"70\"} 형식으로 알려줘";

      try {
        System.out.println(question(prompt));
        JsonNode json = this.objectMapper.readTree(question(prompt));
        String goalName = json.get("goal").asText();
        percentage = json.get("percent").asInt();
        System.out.println(percentage);

        goal = this.jpaGoalRepository.findGoalByName(goalName);
      } catch (Exception e) {
        e.printStackTrace();
        throw new GeneralException(ErrorStatus.INTERNAL_ERROR, "ChatGPT API 호출중 문제가 발생했습니다.");
      }
    }

//    this.jpaTaskRepository.save(
//      Task.builder()
//        .name(request.name())
//        .priority("")
//        .deadline(request.deadline())
//        .score(0L)
//        .maxScore(1000L)
//        .goal(goal)
//        .build()
//    );
  }

  private String question(String prompt) {
    return chatClient.prompt()
      .user(prompt)
      .call()
      .content();
  }
}
