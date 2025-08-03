package isang.orangeplanet.domain.task.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "할일 생성 요청 객체")
public record CreateTaskRequest(

  @Schema(description = "목표 ID")
  String goalId,

  @Schema(description = "할일명")
  String name,

  String priority,

  @Schema(description = "마감일")
  LocalDateTime deadline
) { }
