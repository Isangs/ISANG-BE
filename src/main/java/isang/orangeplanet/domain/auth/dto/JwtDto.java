package isang.orangeplanet.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "Jwt Dto")
public class JwtDto {

  @Schema(description = "Access Token")
  private String accessToken;

  @Schema(description = "Refresh Token")
  private String refreshToken;
}
