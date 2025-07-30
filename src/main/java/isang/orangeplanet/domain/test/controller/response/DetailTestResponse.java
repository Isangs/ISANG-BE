package isang.orangeplanet.domain.test.controller.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class) // 스네이크 케이스 사용을 위함
@Schema(description = "테스트 상세 조회 응답 객체")
public class DetailTestResponse {

  @Schema(description = "테스트 ID")
  private long testId;

  @Schema(description = "제목")
  private String title;

  @Schema(description = "내용")
  private String content;
}
