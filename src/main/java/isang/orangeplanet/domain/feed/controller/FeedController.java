package isang.orangeplanet.domain.feed.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import isang.orangeplanet.domain.feed.controller.dto.request.CompleteTaskWithImageRequest;
import isang.orangeplanet.domain.feed.controller.dto.request.CompleteTaskWithTextRequest;
import isang.orangeplanet.domain.feed.controller.dto.response.FetchFeedListResponse;
import isang.orangeplanet.domain.feed.controller.dto.response.FetchMyFeedListResponse;
import isang.orangeplanet.domain.feed.controller.dto.response.SearchFeedListResponse;
import isang.orangeplanet.domain.feed.service.FeedService;
import isang.orangeplanet.global.api_response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/feed")
@RequiredArgsConstructor
@Tag(name = "Feed", description = "피드 관련 API")
public class FeedController {
  private final FeedService feedService;

  @PostMapping("/text/{taskId}")
  @Operation(summary = "할일 완료 (텍스트)")
  public ApiResponse<Void> completeTaskWithText(
      @RequestBody @Valid CompleteTaskWithTextRequest request,
      @PathVariable Long taskId
  ){
    feedService.completeTaskWithText(taskId, request);
    return ApiResponse.onSuccess();
  }

  @PostMapping("/image/{taskId}")
  @Operation(summary = "할일 완료 (이미지)")
  public ApiResponse<Void> completeTaskWithImage(
      @RequestBody @Valid CompleteTaskWithImageRequest request,
      @PathVariable Long taskId
  ){
    feedService.completeTaskWithImage(taskId, request);
    return ApiResponse.onSuccess();
  }

  @GetMapping
  @Operation(summary = "전체 피드 조회")
  public ApiResponse<FetchFeedListResponse> fetchFeedList(){
    FetchFeedListResponse response = feedService.fetchFeedList();
    return ApiResponse.onSuccess(response);
  }

  @GetMapping("/myself")
  @Operation(summary = "내 피드 조회")
  public ApiResponse<FetchMyFeedListResponse> fetchMyFeedList() {
    FetchMyFeedListResponse response = feedService.fetchMyFeedList();
    return ApiResponse.onSuccess(response);
  }

  @GetMapping("/search")
  @Operation(summary = "피드 검색")
  public ApiResponse<SearchFeedListResponse> searchFeeds(
      @RequestParam String query
  ) {
    SearchFeedListResponse response = feedService.searchFeedList(query);
    return ApiResponse.onSuccess(response);
  }
}
