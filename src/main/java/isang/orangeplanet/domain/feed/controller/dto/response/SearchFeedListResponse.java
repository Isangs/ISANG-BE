package isang.orangeplanet.domain.feed.controller.dto.response;

import isang.orangeplanet.domain.feed.controller.dto.FeedDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchFeedListResponse {
    private List<FeedDto> feeds;
}
