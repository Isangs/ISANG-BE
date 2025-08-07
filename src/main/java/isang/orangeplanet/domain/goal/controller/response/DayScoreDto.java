package isang.orangeplanet.domain.goal.controller.response;

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
@Schema(description = "요일별 점수 조회 응답 Dto")
public class DayScoreDto {

  @Schema(description = "요일")
  private String day;

  @Schema(description = "점수")
  private Long score;
}
