package isang.orangeplanet.domain.test.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 요청 객체는 getter, 불변성 등이 제공되는 record 사용합니다.
 */
@Schema(description = "테스트 생성 요청 객체")
public record CreateTestRequest(

  @Schema(description = "제목")
  String title,

  @Schema(description = "내용")
  String content
) { }
