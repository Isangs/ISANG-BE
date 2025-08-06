package isang.orangeplanet.domain.user.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 수정 요청 객체")
public record UpdateUserRequest(

  @Schema(description = "이름")
  String name,

  @Schema(description = "닉네임")
  String nickName,

  @Schema(description = "소개")
  String introduce,

  @Schema(description = "프로필 사진 URL")
  String profileUrl,

  @Schema(description = "이메일")
  String email
) { }
