package isang.orangeplanet.domain.badge.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import isang.orangeplanet.domain.badge.controller.response.ListBadgeResponse;
import isang.orangeplanet.domain.badge.controller.response.ListDetailBadgeResponse;
import isang.orangeplanet.domain.badge.service.BadgeService;
import isang.orangeplanet.global.api_response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * BadgeController : 뱃지 관련 Controller
 */
@RestController
@RequestMapping(value = "/badge")
@RequiredArgsConstructor
@Tag(name = "Badge", description = "뱃지 관련 API")
public class BadgeController {
  private final BadgeService badgeService;

  /**
   * 획득한 뱃지 목록 조회 엔드포인트
   * @return : 획득한 뱃지 목록 반환
   */
  @GetMapping(value = "/gallery")
  @Operation(summary = "획득한 뱃지 목록 조회", description = "획득한 뱃지 목록 조회 엔드포인트")
  public ApiResponse<ListBadgeResponse> listBadge() {
    return ApiResponse.onSuccess(this.badgeService.listBadge());
  }

  /**
   * 뱃지 갤러리 상세 정보 조회 엔드포인트
   * @return : 뱃지 갤러리 상세 정보 반환
   */
  @GetMapping(value = "/detail")
  @Operation(summary = "획득한 뱃지 상세 정보 조회", description = "획득한 뱃지 상세 정보 조회 엔드포인트")
  public ApiResponse<ListDetailBadgeResponse> listDetailBadge() {
    return ApiResponse.onSuccess(this.badgeService.listDetailBadge());
  }
}
