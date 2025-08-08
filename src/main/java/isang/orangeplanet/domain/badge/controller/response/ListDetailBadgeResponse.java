package isang.orangeplanet.domain.badge.controller.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import isang.orangeplanet.domain.badge.controller.dto.ListDetailBadgeDto;
import lombok.*;

import java.util.List;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "획득한 뱃지 상세 정보 조회 응답 객체")
public class ListDetailBadgeResponse {

  @Schema(description = "뱃지 상세 목록")
  private List<ListDetailBadgeDto> badgeList;
}
