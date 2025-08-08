package isang.orangeplanet.domain.badge.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import isang.orangeplanet.domain.auth.utils.SecurityUtils;
import isang.orangeplanet.domain.badge.controller.response.ListBadgeResponse;
import isang.orangeplanet.domain.badge.controller.response.ListDetailBadgeResponse;
import isang.orangeplanet.domain.badge.service.BadgeService;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.domain.user.repository.UserRepository;
import isang.orangeplanet.domain.user.utils.UserUtils;
import isang.orangeplanet.global.api_response.ApiResponse;
import isang.orangeplanet.global.utils.enums.Badge;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * BadgeController : 뱃지 관련 Controller
 */
@RestController
@RequestMapping(value = "/badge")
@RequiredArgsConstructor
@Tag(name = "Badge", description = "뱃지 관련 API")
public class BadgeController {
  private final BadgeService badgeService;

  private final UserRepository userRepository;

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
  @Operation(summary = "뱃지 갤러리 상세 정보 조회", description = "뱃지 갤러리 상세 정보 조회 엔드포인트")
  public ApiResponse<ListDetailBadgeResponse> listDetailBadge() {
    return ApiResponse.onSuccess(this.badgeService.listDetailBadge());
  }

  // --------------------------------- 테스트 ----------------------------

  @GetMapping(value = "/beginner-escape") // 성공
  public ApiResponse<Void> beginnerEscapeBadgeTest() {
    User user = UserUtils.getUser(SecurityUtils.getAuthUserId());
    this.badgeService.handleBadgeProgress(user, Badge.BEGINNER_ESCAPE);

    return ApiResponse.onSuccess();
  }

  @GetMapping(value = "/three-days/{id}") // 성공
  public ApiResponse<Void> threeDaysBadgeTest(@PathVariable("id") String id) {
    User user = UserUtils.getUser(SecurityUtils.getAuthUserId());
    int score = Badge.calculateScore(user, Badge.THREE_DAY);

    LocalDateTime today = LocalDateTime.now().plusDays(Integer.parseInt(id));
    this.badgeService.applyBadgeLogic(user, Badge.THREE_DAY, score, today);

    return ApiResponse.onSuccess();
  }

  @GetMapping(value = "/perfect-week") // 성공
  public ApiResponse<Void> perfectWeekBadgeTest() {
    User user = UserUtils.getUser(SecurityUtils.getAuthUserId());
    int score = Badge.calculateScore(user, Badge.PERFECT_WEEK);

    LocalDateTime today = LocalDateTime.now();
    this.badgeService.applyBadgeLogic(user, Badge.PERFECT_WEEK, score, today);

    return ApiResponse.onSuccess();
  }

  @GetMapping(value = "/monthly-king") // 성공!!!!!!!!!!
  public ApiResponse<Void> monthlyKingBadgeTest() {
    User user = UserUtils.getUser(SecurityUtils.getAuthUserId());
    int score = Badge.calculateScore(user, Badge.MONTHLY_KING);

    LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
    LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

    // 한달치
    for (int i = 0; i < 30; i++) {
      User topUser = this.userRepository.getTopRankerForToday(startOfDay, endOfDay);
      LocalDateTime today = LocalDateTime.now().plusDays(i);
      this.badgeService.applyBadgeLogic(topUser, Badge.MONTHLY_KING, score, today);
    }

    return ApiResponse.onSuccess();
  }
}
