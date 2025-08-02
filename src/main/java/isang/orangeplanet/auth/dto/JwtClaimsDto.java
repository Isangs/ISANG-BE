package isang.orangeplanet.auth.dto;

import isang.orangeplanet.global.utils.enums.Role;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JwtClaimsDto {
  private String nickName;
  private Role role;
}
