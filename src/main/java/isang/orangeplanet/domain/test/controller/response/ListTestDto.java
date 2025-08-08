package isang.orangeplanet.domain.test.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "테스트 목록 조회 응답 DTO")
public class ListTestDto {

  @Schema(description = "테스트 ID")
  private long testId;

  @Schema(description = "제목")
  private String title;
}
