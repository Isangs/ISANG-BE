package isang.orangeplanet.domain.badge.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import isang.orangeplanet.domain.badge.BadgeProgress;
import isang.orangeplanet.global.utils.enums.Badge;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static isang.orangeplanet.domain.badge.QBadgeProgress.badgeProgress;

@Repository
@RequiredArgsConstructor
public class BadgeRepository {
  private final JPAQueryFactory queryFactory;

  /**
   * 획득한 뱃지 목록 조회
   * @param userId : 회원 ID
   * @return : 획득한 뱃지 목록 반환
   */
  public List<Badge> listBadge(String userId) {
    return this.queryFactory
      .select(badgeProgress.badge)
      .from(badgeProgress)
      .where(
        badgeProgress.user.userId.eq(userId)
          .and(badgeProgress.isAchieved.eq(true))
      )
      .fetch();
  }

  public List<BadgeProgress> listDetailBadge(String userId) {
    return this.queryFactory.select(badgeProgress)
      .from(badgeProgress)
      .where(badgeProgress.user.userId.eq(userId))
      .fetch();
  }
}
