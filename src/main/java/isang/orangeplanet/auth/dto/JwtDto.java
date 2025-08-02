package isang.orangeplanet.auth.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtDto {
  private String accessToken;
  private String refreshToken;
}
