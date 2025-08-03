package isang.orangeplanet.domain.user.controller.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import isang.orangeplanet.global.utils.enums.Role;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "회원 상세 정보 조회 응답 객체")
public class DetailUserResponse {

  @Schema(description = "회원 ID")
  private String userId;

  @Schema(description = "이름")
  private String name;

  @Schema(description = "닉네임")
  private String nickName;

  @Schema(description = "소개")
  private String introduce;

  @Schema(description = "프로필 사진 URL")
  private String profileUrl;

  @Schema(description = "이메일")
  private String email;

  @Schema(description = "권한")
  private Role role;

  @Schema(description = "레벨")
  private int level;

  @Schema(description = "총 점수")
  private long totalScore;
}
