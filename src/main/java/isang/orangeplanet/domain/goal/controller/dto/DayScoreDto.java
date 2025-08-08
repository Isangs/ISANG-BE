package isang.orangeplanet.domain.goal.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "요일별 점수 조회 응답 Dto")
public class DayScoreDto {

  @Schema(description = "요일")
  private String day;

  @Schema(description = "점수")
  private Long score;
}
