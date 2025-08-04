package isang.orangeplanet.domain.task.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import isang.orangeplanet.domain.goal.controller.response.GetGoalResponse;
import isang.orangeplanet.global.utils.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "할일 목록 조회 응답 Dto")
public class ListTaskDto {

  @Schema(description = "할일 ID")
  private long taskId;

  @Schema(description = "목표")
  private GetGoalResponse goal;

  @Schema(description = "이름")
  private String name;

  @Schema(description = "우선순위")
  private Priority priority;

  @Schema(description = "현재 점수 백분율")
  private int percentageScore;

  @Schema(description = "마감일")
  private LocalDateTime deadline;
}
