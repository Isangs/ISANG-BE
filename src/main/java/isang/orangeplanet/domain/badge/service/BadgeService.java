package isang.orangeplanet.domain.badge.service;

import isang.orangeplanet.domain.auth.utils.SecurityUtils;
import isang.orangeplanet.domain.badge.BadgeProgress;
import isang.orangeplanet.domain.badge.controller.dto.ListBadgeDto;
import isang.orangeplanet.domain.badge.controller.dto.ListDetailBadgeDto;
import isang.orangeplanet.domain.badge.controller.response.ListBadgeResponse;
import isang.orangeplanet.domain.badge.controller.response.ListDetailBadgeResponse;
import isang.orangeplanet.domain.badge.repository.BadgeRepository;
import isang.orangeplanet.domain.badge.repository.JpaBadgeProgressRepository;
import isang.orangeplanet.domain.badge.repository.JpaBadgeRepository;
import isang.orangeplanet.domain.task.repository.TaskRepository;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.domain.user.repository.UserRepository;
import isang.orangeplanet.domain.user.utils.UserUtils;
import isang.orangeplanet.global.utils.enums.Badge;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * BadgeService : 뱃지 관련 Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BadgeService {

  /**
   * 리펙토링은 나중에! 일단 돌아가게 구현하자.
   */

  private final JpaBadgeProgressRepository jpaBadgeProgressRepository;
  private final JpaBadgeRepository jpaBadgeRepository;
  private final BadgeRepository badgeRepository;
  private final TaskRepository taskRepository;
  private final UserRepository userRepository;

  /**
   * 획득한 뱃지 목록 조회 메서드
   * @return : 획득한 뱃지 목록 반환
   */
  public ListBadgeResponse listBadge() {
    List<ListBadgeDto> responses = new ArrayList<>();

    List<Badge> badgeList = this.badgeRepository.listBadge(SecurityUtils.getAuthUserId());
    badgeList.forEach(badge ->
      responses.add(
        ListBadgeDto.builder()
          .badge(badge)
          .name(badge.getName())
          .build()
      )
    );

    return ListBadgeResponse.builder()
      .badgeList(responses)
      .build();
  }

  /**
   * 뱃지 갤러리 상세 정보 조회
   * @return : 뱃지 갤러리 상세 정보 반환
   */
  public ListDetailBadgeResponse listDetailBadge() {
    List<ListDetailBadgeDto> responses = new ArrayList<>();

    List<BadgeProgress> badgeList = this.badgeRepository.listDetailBadge(SecurityUtils.getAuthUserId());
    badgeList.forEach(bp ->
      responses.add(
        ListDetailBadgeDto.builder()
          .badge(bp.getBadge())
          .name(bp.getBadge().getName())
          .desc(bp.getBadge().getDesc())
          .isAchieved(bp.isAchieved())
          .progress(bp.getProgress())
          .condition(bp.getBadge().getCondition())
          .build()
      )
    );

    return ListDetailBadgeResponse.builder()
      .badgeList(responses)
      .build();
  }

  /**
   * 오늘 1등인 회원을 거르기 위한 스케줄러 (매일 00:00)
   */
  @Scheduled(cron = "0 0 0 * * *")
  public void updateDailyTopUserScheduler() {
    LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
    LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

    User topUser = this.userRepository.getTopRankerForToday(startOfDay, endOfDay);
    this.handleBadgeProgress(topUser, Badge.MONTHLY_KING);
  }

  /**
   * 일주일 모든 할일을 완료했느냐!를 판단하기 위해.. (매주 일요일 23:59:59)
   */
  @Scheduled(cron = "59 59 23 * * 0")
  public void perfectWeekScheduler() {
    User user = UserUtils.getUser(SecurityUtils.getAuthUserId());
    this.handleBadgeProgress(user, Badge.PERFECT_WEEK);
  }

  /**
   * 뱃지 진행도와 획득 여부 확인 메서드(?)
   * @param badge : 뱃지 Enum
   */
  public void handleBadgeProgress(User user, Badge badge) {
    BadgeProgress progress = this.jpaBadgeProgressRepository.findBadgeProgressesByUserAndBadge(user, badge);
    int score = Badge.calculateScore(user, badge);

    // 진행도 누적 방식
    if (badge == Badge.THREE_DAY || badge == Badge.PERFECT_WEEK || badge == Badge.MONTHLY_KING) {
      this.applyBadgeLogic(progress, user, badge, score);
      return;
    }

    // BadgeProgress가 null이면 생성하고 끝내라 이거임
    if (this.getOrCreateBadgeProgress(progress, badge, user, score) == null) return;

    // 이미 획득한 경우 그냥 종료
    if (progress.isAchieved()) return;

    // 뱃지가 아래 세가지면 누적 방식이 아님! 그냥 업데이트하는 방식
    if (badge == Badge.MASTER || badge == Badge.BEGINNER_ESCAPE || badge == Badge.OVER_PERFECT_SCORES) {
      this.applyProgress(progress, score, badge);
    }
  }

  /**
   * 일단 아래 Badge Enum들은 누적되는 방식이기 때문에 기존 점수에 새로운 점수를 더하여 계산하고 progress가 없으면 새로 생성까지 해주는 메서드(?)
   * (말로 설명하니까 뭔가 잘안된다..)
   * @param progress : Nullable한 BadgeProgress 객체
   * @param user : User 객체
   * @param badge : Badge Enum
   * @param score : 각 Badge Enum의 점수
   */
  public void applyBadgeLogic(@Nullable BadgeProgress progress, User user, Badge badge, int score) {
    progress = this.getOrCreateBadgeProgress(progress, badge, user, score); // BadgeProgress가 null일시 생성 후 BadgeProgress 객체 반환
    /*
      뭐가 이리 복잡하지 ㅋㅋㅋㅋㅋㅋㅋㅋㅋ 아놔 머리 아파
      아래 세가지 뱃지는 전부 누적되는 방식이기 때문에 증감하여 새로운 진행도(점수)를 만듦 (기존 점수 += score)
      할일 완료시 호출(테스트를 위함) -> 날자 변경해가면서 테스트 돌리면 될듯?
     */
    switch (badge) {
      case THREE_DAY -> this.handleThreeDayBadge(progress, badge, score);
      case MONTHLY_KING -> this.handleMonthlyKingBadge(progress, user, badge, score);
      case PERFECT_WEEK -> this.handlePerfectWeekBadge(progress, badge, user);
    }
  }

  /**
   * THREE_DAY 뱃지 획득 진행도 파악
   * @param progress : BadgeProgress 객체
   * @param badge : Badge Enum
   * @param score : 진행도(점수)
   */
  public void handleThreeDayBadge(BadgeProgress progress, Badge badge, int score) {
    LocalDateTime today = LocalDateTime.now();
    LocalDateTime lastDate = progress.getLastCompletedAt();
    int newProgress = progress.getProgress();

    if (lastDate != null && lastDate.toLocalDate().plusDays(1).equals(today.toLocalDate())) {
      // 연속 유지하면서 획득 조건 체크~!
      newProgress += score;
      progress.updateLastCompletedAt(today); // 연속 체크를 해야하기 때문에 추가했음
      this.applyProgress(progress, newProgress, badge);
    } else if (lastDate != null && lastDate.toLocalDate().equals(today.toLocalDate())) {
      // 똑같은 하루에 할일을 추가로 완료하면 X, 기존 score로 유지
//      progress.updateProgress(progress.getProgress());
      return;
    } else { // 연속 실패!! (다시 1일부터)
      // lastCompletedAt -> today 업데이트
      progress.updateLastCompletedAt(today);
      progress.updateProgress(1);
    }
  }

  /**
   * MONTHLY_KING 뱃지 획득 진행도 파악
   * @param progress : BadgeProgress 객체
   * @param user : 회원 객체
   * @param badge : Badge Enum
   * @param score : 진행도(점수)
   */
  public void handleMonthlyKingBadge(BadgeProgress progress, User user, Badge badge, int score) {
    LocalDate date = LocalDate.now();
    String currentUserId = SecurityUtils.getAuthUserId();
    LocalDate lastDate = (progress.getLastCompletedAt() != null) ? progress.getLastCompletedAt().toLocalDate() : null;
    int newProgress = progress.getProgress();

    // 로그인한 회원이 오늘 1등이 아니면 아래 실행해서 메서드 종료
    if (!user.getUserId().equals(currentUserId)) return;

    if (lastDate != null && lastDate.plusDays(1).equals(date)) {
      // 전날까지 연속 유지
      newProgress += score;
      progress.updateLastMaintainedAt(date); // 다음 연속 체크를 위한 날짜 업데이트
      this.applyProgress(progress, newProgress, badge);
    } else {

      // 오늘 날짜와 같다면
      if (lastDate != null && lastDate.isEqual(progress.getLastCompletedAt().toLocalDate())) {
        log.info("오늘 이미 처리된 상태입니다. (날짜 중복 처리 방지)");
        return;
      }

      // 연속 실패 (중간에 하루라도 1등 실패하면!)
      progress.updateLastMaintainedAt(date);
      progress.updateProgress(1);

      log.info("사용자 {}의 '{}' 뱃지 연속 조건 실패 - 진행도 1로 초기화", user.getName(), badge.name());
    }
  }

  /**
   * PERFECT_WEEK 뱃지 획득 진행도 파악
   * @param progress : BadgeProgress 객체
   * @param badge : Badge Enum
   * @param user : 회원 객체
   */
  public void handlePerfectWeekBadge(BadgeProgress progress, Badge badge, User user) {
    LocalDate date = LocalDate.now();
    LocalDate startOfWeek = date.with(DayOfWeek.MONDAY);
    LocalDate endOfWeek = date.with(DayOfWeek.SUNDAY);

    // 이번 주 생성된 할일 중 완료되지 않은 것이 있는지 체크
    boolean hasUncompleted = this.taskRepository.isPerfectWeek(
      user.getUserId(), startOfWeek.atStartOfDay(), endOfWeek.atTime(LocalTime.MAX)
    );

    if (hasUncompleted) {
      this.applyProgress(progress, 7, badge); // 7점 달성 (바로 뱃지 획득)
    } else {
      log.info("'{}' 뱃지 사용자 {}의 이번 주 미완료 항목 존재 - 진행도 초기화", badge.name(), user.getName());
    }
  }

  /**
   * BadgeProgress의 null 여부 확인 및 생성
   * @param progress : BadgeProgress 객체
   * @param badge : Badge Enum
   * @param user : 회원 객체
   * @param score : 진행도(점수)
   * @return : 생성된 BadgeProgress 객체
   */
  private BadgeProgress getOrCreateBadgeProgress(@Nullable BadgeProgress progress, Badge badge, User user, int score) {
    if (progress == null) {
      BadgeProgress badgeProgress = BadgeProgress.builder()
        .badge(badge)
        .isAchieved(false)
        .lastCompletedAt(LocalDateTime.now())
        .lastMaintainedAt(LocalDate.now())
        .progress(score)
        .user(user)
        .build();

      this.jpaBadgeProgressRepository.save(badgeProgress);
      return badgeProgress;
    } else {
      return progress;
    }
  }

  /**
   * 뱃지 획득 조건과 진행도 비교 (true: 획득 O, false: 획득 X)
   * @param progress : BadgeProgress Entity
   * @param score : 진행도(점수)
   * @param badge : 뱃지 Enum
   */
  private void applyProgress(BadgeProgress progress, int score, Badge badge) {
    // 획득 조건이랑 해당 진행도(점수)와 비교
    if (score >= badge.getCondition()) {
      // 획득 조건보다 크거가 같으면 progress와 isAchieved 업데이트
      progress.updateProgress(score);
      progress.updateIsAchieved(true);

      // 뱃지 획득
      this.jpaBadgeRepository.save(
        isang.orangeplanet.domain.badge.Badge.builder()
          .progress((long) progress.getProgress())
          .isAchieved(true)
          .badge(badge)
          .user(progress.getUser())
          .build()
      );

      log.info("'{}' 뱃지 달성! (설명: {})", badge.getName(), badge.getDesc());
    } else {
      // 아직 진행도(점수)가 낮다면 계속 progress 업데이트
      progress.updateProgress(score);
      log.info("'{}' 진행 중... 현재 {}/{} (설명: {})",
        badge.getName(), progress.getProgress(), badge.getCondition(), badge.getDesc()
      );
    }
  }
}
