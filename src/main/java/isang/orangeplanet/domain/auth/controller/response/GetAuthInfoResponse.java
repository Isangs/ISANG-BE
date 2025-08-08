package isang.orangeplanet.domain.auth.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그인 응답 객체")
public class GetAuthInfoResponse {

  @Schema(description = "Access Token")
  private String accessToken;

  @Schema(description = "Refresh Token")
  private String refreshToken;

  @Schema(description = "권한")
  private String role;
}
