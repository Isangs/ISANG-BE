package isang.orangeplanet.domain.goal.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "목표별 요일 목록 조회 Dto")
public class ListDayOfWeekDto {

  @Schema(description = "요일")
  private String day;

  @Schema(description = "점수")
  private Long score;

  public void updateDayUpperCase(String day) {
    this.day = day.toUpperCase();
  }
}
