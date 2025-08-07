package isang.orangeplanet.domain.badge.service;

import isang.orangeplanet.domain.auth.utils.SecurityUtils;
import isang.orangeplanet.domain.badge.BadgeProgress;
import isang.orangeplanet.domain.badge.controller.dto.ListBadgeDto;
import isang.orangeplanet.domain.badge.controller.response.ListBadgeResponse;
import isang.orangeplanet.domain.badge.repository.BadgeRepository;
import isang.orangeplanet.domain.badge.repository.JpaBadgeProgressRepository;
import isang.orangeplanet.domain.task.repository.TaskRepository;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.domain.user.repository.UserRepository;
import isang.orangeplanet.domain.user.utils.UserUtils;
import isang.orangeplanet.global.utils.enums.Badge;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class BadgeService {

  private final BadgeRepository badgeRepository;
  private final JpaBadgeProgressRepository jpaBadgeProgressRepository;
  private final UserRepository userRepository;
  private final TaskRepository taskRepository;

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

  // 오늘 1등인 회원을 거르기 위한 스케줄러
  @Scheduled(cron = "0 0 0 * * *") // 매일 00:00
  public void updateDailyTopUser() {
    LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
    LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

    User topUser = this.userRepository.getTopRankerForToday(startOfDay, endOfDay);
    this.handleBadgeProgress(topUser, Badge.MONTHLY_KING);
  }

  // 일주일 모든 할일을 완료했느냐!를 판단하기 위해..
  @Scheduled(cron = "59 59 23 * * 0") // 매주 일요일 23:59:59
  public void perfectWeek() {
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

    if(progress == null) {
      this.jpaBadgeProgressRepository.save(
        BadgeProgress.builder()
          .badge(badge)
          .isAchieved(false)
          .lastCompletedAt(LocalDateTime.now())
          .lastMaintainedAt(LocalDate.now())
          .progress(score)
          .user(user)
          .build()
      );

      return;
    }

    // 이미 획득한 경우 그냥 종료
    if (progress.isAchieved()) return;

    int newProgress = progress.getProgress();

    // 뱃지가 아래 세가지면 누적 방식이 아님! 그냥 업데이트하는 방식
    if (badge == Badge.MASTER || badge == Badge.BEGINNER_ESCAPE || badge == Badge.OVER_PERFECT_SCORES) {
      this.applyProgress(progress, score, badge);
    } else {
      // 위 세가지 뱃지가 아니면 전부 누적되는 방식이기 때문에 증감하여 새로운 진행도(점수)를 만듦 (기존 점수 += score)

      /*
        뭐가 이리 복잡하지 ㅋㅋㅋㅋㅋㅋㅋㅋㅋ 아놔 머리 아파
       */
      LocalDate date = LocalDate.now();
      switch (badge) {
        case THREE_DAY -> { // -------------------------------------------> 테스트 잘됨!! ㄴㅇㅅ!
          LocalDateTime today = LocalDateTime.now();
          LocalDateTime lastDate = progress.getLastCompletedAt();

          if (lastDate != null && lastDate.toLocalDate().plusDays(1).equals(today.toLocalDate())) {
            // 연속 유지하면서 획득 조건 체크~!
            newProgress += score;
            progress.updateLastCompletedAt(today); // 연속 체크를 해야하기 때문에 추가했음
            this.applyProgress(progress, newProgress, badge);
          } else if (lastDate != null && lastDate.toLocalDate().equals(today.toLocalDate())) {
            // 똑같은 하루에 할일을 추가로 완료하면 X, 기존 score로 유지
//            progress.updateProgress(progress.getProgress());
            return;
          } else { // 연속 실패!! (다시 1일부터)
            // lastCompletedAt -> today 업데이트
            progress.updateLastCompletedAt(today);
            progress.updateProgress(1);
          }
        }

        // 아래 둘다 테스트가 복잡하다...
        case MONTHLY_KING -> {
          LocalDate lastDate = (progress.getLastCompletedAt() != null) ? progress.getLastCompletedAt().toLocalDate() : null;
          String currentUserId = SecurityUtils.getAuthUserId();

          // 로그인한 회원이 오늘 1등이 아니면 아래 실행해서 메서드 종료
          if (!user.getUserId().equals(currentUserId)) return;

          if (lastDate != null && lastDate.plusDays(1).equals(date)) {
            // 전날까지 연속 유지
            newProgress += score;
            progress.updateLastMaintainedAt(date); // 다음 연속 체크를 위한 날짜 업데이트
            this.applyProgress(progress, newProgress, badge);
          } else {
            // 연속 실패 (중간에 하루라도 1등 실패하면!)
            progress.updateLastMaintainedAt(date);
            progress.updateProgress(1);
          }
        }
        case PERFECT_WEEK -> {
          LocalDate startOfWeek = date.with(DayOfWeek.MONDAY);
          LocalDate endOfWeek = date.with(DayOfWeek.SUNDAY);

          // 이번 주 생성된 할일 중 완료되지 않은 것이 있는지 체크
          boolean hasUncompleted = this.taskRepository.isPerfectWeek(
            user.getUserId(), startOfWeek.atStartOfDay(), endOfWeek.atTime(LocalTime.MAX)
          );

          if (hasUncompleted) {
            this.applyProgress(progress, 7, badge); // 7점 달성 (바로 뱃지 획득)
          } else {
            progress.updateProgress(1); // 이번 주 실패 (다음 주 다시 시작)
          }
        }
      } // 에휴.. 힘들었다..


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

      /*
        실제 뱃지 획득 로직 구현 해야함. 근데 아직 안함 ㅎ..
       */

      log.info("{} 뱃지 획득!", badge);
    } else {
      // 아직 진행도(점수)가 낮다면 계속 progress 업데이트
      progress.updateProgress(score);
      log.info("{} 아직!", badge);
    }
  }
}
