package isang.orangeplanet.domain.feed.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import isang.orangeplanet.domain.feed.controller.dto.request.CompleteTaskWithImageRequest;
import isang.orangeplanet.domain.feed.controller.dto.request.CompleteTaskWithTextRequest;
import isang.orangeplanet.domain.feed.controller.dto.response.FetchFeedListResponse;
import isang.orangeplanet.domain.feed.controller.dto.response.SearchFeedListResponse;
import isang.orangeplanet.domain.feed.service.FeedService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/feed")
@RequiredArgsConstructor
@Tag(name = "Feed", description = "피드 관련 API")
public class FeedController {
  private final FeedService feedService;

  @PostMapping("/text/{taskId}")
  @Operation(summary = "할일 완료 (텍스트)")
  public ResponseEntity<Void> completeTaskWithText(
      @RequestBody @Valid CompleteTaskWithTextRequest request,
      @PathVariable Long taskId
  ){
    feedService.completeTaskWithText(taskId, request);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/image/{taskId}")
  @Operation(summary = "할일 완료 (이미지)")
  public ResponseEntity<Void> completeTaskWithImage(
      @RequestBody @Valid CompleteTaskWithImageRequest request,
      @PathVariable Long taskId
  ){
    feedService.completeTaskWithImage(taskId, request);
    return ResponseEntity.ok().build();
  }

  @GetMapping
  @Operation(summary = "전체 피드 조회")
  public ResponseEntity<FetchFeedListResponse> fetchFeeds(){
    FetchFeedListResponse response = feedService.fetchFeedList();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/search")
  @Operation(summary = "피드 검색")
  public ResponseEntity<SearchFeedListResponse> searchFeeds(
      @RequestParam String query
  ) {
    SearchFeedListResponse response = feedService.searchFeedList(query);
    return ResponseEntity.ok(response);
  }
}
