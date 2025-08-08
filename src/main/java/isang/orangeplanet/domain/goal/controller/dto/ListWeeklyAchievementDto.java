package isang.orangeplanet.domain.goal.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "주간 성과 조회 응답 Dto")
public class ListWeeklyAchievementDto {

  @Schema(description = "목표 ID")
  private Long goalId;

  @Schema(description = "목표 이름")
  private String name;

  @Schema(description = "요일")
  private String day;

  @Schema(description = "점수")
  private Long score;

  @Schema(description = "최대 점수")
  private Long maxScore;
}
