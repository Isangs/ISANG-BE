package isang.orangeplanet.domain.task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import isang.orangeplanet.domain.task.controller.request.CreateTaskRequest;
import isang.orangeplanet.domain.task.service.TaskService;
import isang.orangeplanet.global.api_response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TaskController : 할일 관련 Controller
 */
@RestController
@RequestMapping(value = "/task")
@RequiredArgsConstructor
@Tag(name = "Task", description = "할일 관련 API")
public class TaskController {
  private final TaskService taskService;

  /**
   * 할일 추가(생성) 엔드포인트
   * @param request : CreateTaskRequest 객체
   * @return : 공통 응답 객체 반환
   */
  @PostMapping(value = "/create")
  @Operation(summary = "할일 생성", description = "할일 생성 엔드포인트")
  public ApiResponse<Void> createTask(@RequestBody CreateTaskRequest request) {
    taskService.createTask(request);
    return ApiResponse.onSuccess();
  }
}
