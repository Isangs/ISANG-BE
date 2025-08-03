package isang.orangeplanet.domain.goal.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "목표 생성 요청 객체")
public record CreateGoalRequest(

  @Schema(description = "이름")
  String name,

  @Schema(description = "색상 코드")
  String colorCode
) { }
