package isang.orangeplanet.domain.task.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FetchTaskVisibilityResponse {
  private Boolean isAddFeed;

  private Boolean isPublic;
}
