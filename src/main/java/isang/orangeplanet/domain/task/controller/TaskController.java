package isang.orangeplanet.domain.task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import isang.orangeplanet.domain.task.controller.request.CreateTaskRequest;
import isang.orangeplanet.domain.task.controller.request.UpdateTaskRequest;
import isang.orangeplanet.domain.task.controller.response.ListTaskResponse;
import isang.orangeplanet.domain.task.controller.response.FetchTaskVisibilityResponse;
import isang.orangeplanet.domain.task.service.TaskService;
import isang.orangeplanet.global.api_response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
  @PostMapping
  @Operation(summary = "할일 생성", description = "할일 생성 엔드포인트")
  public ApiResponse<Void> createTask(@RequestBody CreateTaskRequest request) {
    taskService.createTask(request);
    return ApiResponse.onSuccess();
  }

  /**
   * 전체 할일 목록 조회 엔드포인트
   * @return : 전체 할일 목록 반환
   */
  @GetMapping(value = "/list")
  @Operation(summary = "전체 할일 목록 조회", description = "전체 할일 목록 조회 엔드포인트")
  public ApiResponse<ListTaskResponse> getTaskList() {
    return ApiResponse.onSuccess(taskService.getTaskList());
  }

  /**
   * 특정 할일 삭제 엔드포인트
   * @param taskId : 할일 ID
   * @return : 공통 응답 객체 반환
   */
  @DeleteMapping(value = "/{id}")
  @Operation(summary = "특정 할일 삭제", description = "특정 할일 삭제 엔드포인트")
  public ApiResponse<Void> deleteTask(@PathVariable("id") String taskId) {
    this.taskService.deleteTask(taskId);
    return ApiResponse.onSuccess();
  }

  /**
   * 특정 할일 수정 엔드포인트
   * @param id 할일 ID
   * @param request 할일 수정 DTO
   * @return 응답 값이 없는 ResponseEntity
   */
  @PatchMapping("/{id}")
  @Operation(summary = "특정 할일 수정", description = "특정 할일 수정 엔드포인트")
  public ResponseEntity<Void> updateTask(
      @PathVariable Long id,
      @RequestBody @Valid UpdateTaskRequest request
  ) {
    taskService.updateTask(id, request);
    return ResponseEntity.ok().build();
  }

  /**
   * 특정 할일 설정 조회 엔드포인트
   * @param id 할일 ID
   * @return DTO를 포함한 ResponseEntity
   */
  @GetMapping("/{id}/setting")
  @Operation(summary = "특정 할일 설정 조회", description = "특정 할일 설정 조회 엔드포인트")
  public ResponseEntity<FetchTaskVisibilityResponse> fetchTaskVisibility(
      @PathVariable Long id
  ) {
    FetchTaskVisibilityResponse response = taskService.getTaskById(id);
    return ResponseEntity.ok(response);
  }
}
