package isang.orangeplanet.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "카카오 사용자 정보 응답 Dto")
public class KakaoUserDto {

  @Schema(description = "사용자 고유 ID")
  private String kakaoUserId;

  @Schema(description = "이름")
  private String nickName;

  @Schema(description = "프로필 사진 URL")
  private String profileUrl;

  @Schema(description = "이메일")
  private String email;
}
