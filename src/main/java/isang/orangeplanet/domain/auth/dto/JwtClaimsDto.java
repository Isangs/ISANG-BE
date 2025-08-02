package isang.orangeplanet.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import isang.orangeplanet.global.utils.enums.Role;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "Jwt 정보 Dto")
public class JwtClaimsDto {

  @Schema(description = "회원 ID")
  private String userId;

  @Schema(description = "권한")
  private Role role;
}
