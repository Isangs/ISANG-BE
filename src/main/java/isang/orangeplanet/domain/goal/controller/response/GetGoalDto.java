package isang.orangeplanet.domain.goal.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "목표 조회 응답 객체")
public class GetGoalDto {

  @Schema(description = "목표 ID")
  private long goalId;

  @Schema(description = "이름")
  private String name;

  @Schema(description = "할일 점수")
  private Integer score;

  @Schema(description = "할일 최대 점수")
  private long maxScore;

  @Schema(description = "색상 코드")
  private String colorCode;
}
