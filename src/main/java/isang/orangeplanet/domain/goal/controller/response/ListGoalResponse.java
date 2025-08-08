package isang.orangeplanet.domain.goal.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "목표 조회 응답 객체")
public class ListGoalResponse {

  @Schema(description = "목표 목록")
  List<GetGoalResponse> goalList;
}
