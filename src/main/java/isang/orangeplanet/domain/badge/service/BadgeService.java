package isang.orangeplanet.domain.badge.service;

import isang.orangeplanet.domain.auth.utils.SecurityUtils;
import isang.orangeplanet.domain.badge.BadgeProgress;
import isang.orangeplanet.domain.badge.repository.BadgeRepository;
import isang.orangeplanet.domain.badge.repository.JpaBadgeProgressRepository;
import isang.orangeplanet.domain.badge.repository.JpaBadgeRepository;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.domain.user.utils.UserUtils;
import isang.orangeplanet.global.utils.enums.Badge;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BadgeService {
  private final ApplicationEventPublisher eventPublisher;
  private final JpaBadgeRepository jpaBadgeRepository;
  private final BadgeRepository badgeRepository;
  private final JpaBadgeProgressRepository jpaBadgeProgressRepository;

  /**
   * 뱃지 진행도와 획득 여부 확인 메서드(?)
   * @param badge : 뱃지 Enum
   */
  public void handleBadgeProgress(Badge badge) {
    User user = UserUtils.getUser(SecurityUtils.getAuthUserId());
    BadgeProgress progress = this.jpaBadgeProgressRepository.findBadgeProgressesByUserAndBadge(user, badge);

    int score = Badge.calculateScore(user, badge);

    if(progress == null) {
      this.jpaBadgeProgressRepository.save(
        BadgeProgress.builder()
          .badge(badge)
          .isAchieved(false)
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
      newProgress += score;
      this.applyProgress(progress, newProgress, badge);
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
    }
  }
}
