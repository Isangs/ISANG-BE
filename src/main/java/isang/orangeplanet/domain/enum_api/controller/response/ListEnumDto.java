package isang.orangeplanet.domain.enum_api.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Enum 목록 조회 DTO")
public class ListEnumDto {

  @Schema(description = "enum 코드")
  private String code;

  @Schema(description = "뱃지 이름")
  private String badge;

  @Schema(description = "설명")
  private String desc;

  @Schema(description = "달성 조건")
  private int condition;
}
