package isang.orangeplanet.domain.auth.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Access Token 재발급 응답 객체")
public class GetTokenResponse {

  @Schema(description = "Access Token")
  private String accessToken;
}
