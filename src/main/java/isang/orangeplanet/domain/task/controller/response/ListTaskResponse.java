package isang.orangeplanet.domain.task.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "할일 목록 조회 응답 객체")
public class ListTaskResponse {

  @Schema(description = "할일 목록")
  private List<ListTaskDto> taskList;
}
