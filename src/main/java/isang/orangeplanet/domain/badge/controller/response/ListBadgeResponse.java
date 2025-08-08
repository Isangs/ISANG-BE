package isang.orangeplanet.domain.badge.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import isang.orangeplanet.domain.badge.controller.dto.ListBadgeDto;
import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "획득한 뱃지 목록 조회 응답 객체")
public class ListBadgeResponse {

  @Schema(description = "뱃지 목록")
  private List<ListBadgeDto> badgeList;
}
