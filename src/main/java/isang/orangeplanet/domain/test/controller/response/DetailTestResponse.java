package isang.orangeplanet.domain.test.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "테스트 상세 조회 응답 객체")
public class DetailTestResponse {

  @Schema(description = "테스트 ID")
  @JsonProperty(namespace = "test_id")
  private long testId;

  @Schema(description = "제목")
  private String title;

  @Schema(description = "내용")
  private String content;
}
