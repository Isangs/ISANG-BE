package isang.orangeplanet.domain.goal.controller.response;

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
@Schema(description = "목표 상세 목록 조회 응답 객체")
public class ListGoalScoresResponse {

  @Schema(description = "목표 목록")
  private List<ListGoalScoresDto> goalList;
}
