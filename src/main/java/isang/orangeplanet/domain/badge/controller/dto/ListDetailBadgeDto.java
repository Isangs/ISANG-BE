package isang.orangeplanet.domain.badge.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import isang.orangeplanet.global.utils.enums.Badge;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "획득한 뱃지 상세 정보 조회 응답 Dto")
public class ListDetailBadgeDto {

  @Schema(description = "뱃지 code")
  private Badge badge;

  @Schema(description = "이름")
  private String name;

  @Schema(description = "설명")
  private String desc;

  @Schema(description = "획득 여부")
  private Boolean isAchieved;

  @Schema(description = "진행도")
  private Integer progress;

  @Schema(description = "달성 조건 (최대 진행도)")
  private Integer condition;
}
