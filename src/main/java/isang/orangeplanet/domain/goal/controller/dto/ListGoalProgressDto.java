package isang.orangeplanet.domain.goal.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "목표 달성률 조회 응답 Dto")
public class ListGoalProgressDto {

  @Schema(description = "목표 ID")
  private Long goalId;

  @Schema(description = "이름")
  private String name;

  @Schema(description = "달성률")
  private Integer percentage;
}
