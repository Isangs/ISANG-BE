package isang.orangeplanet.domain.goal.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "주간 성과 목록 조회 Dto")
public class WeeklyGoalAchievementDto {

  @Schema(description = "목표 ID")
  private Long goalId;

  @Schema(description = "목표 이름")
  private String name;

  @Schema(description = "총 점수")
  private Integer totalScore;

  @Schema(description = "최대 점수")
  private Integer maxScore;

  @Schema(description = "요일별 점수 목록")
  private List<DayScoreDto> dayList;

  public void setTotalScore(int totalScore) {
    this.totalScore = totalScore;
  }
}
