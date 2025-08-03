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
@Schema(description = "목표 조회 응답 객체")
public class GetGoalResponse {

  @Schema(description = "목표 ID")
  private long goalId;

  @Schema(description = "이름")
  private String name;

  @Schema(description = "색상 코드")
  private String colorCode;
}
