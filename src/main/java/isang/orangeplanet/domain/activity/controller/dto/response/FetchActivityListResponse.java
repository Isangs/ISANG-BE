package isang.orangeplanet.domain.activity.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FetchActivityListResponse {
    private List<FetchActivityResponse> activities;
}
