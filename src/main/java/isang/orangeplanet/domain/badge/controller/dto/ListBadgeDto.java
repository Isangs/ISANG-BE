package isang.orangeplanet.domain.badge.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import isang.orangeplanet.global.utils.enums.Badge;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "획득한 뱃지 목록 응답 Dto")
public class ListBadgeDto {

  @Schema(description = "뱃지 code")
  private Badge badge;

  @Schema(description = "이름")
  private String name;
}
