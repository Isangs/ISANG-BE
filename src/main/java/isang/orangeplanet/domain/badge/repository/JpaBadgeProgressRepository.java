package isang.orangeplanet.domain.badge.repository;

import isang.orangeplanet.domain.badge.BadgeProgress;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.global.utils.enums.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaBadgeProgressRepository extends JpaRepository<BadgeProgress, Long> {
  BadgeProgress findBadgeProgressesByUserAndBadge(User user, Badge badge);
  List<BadgeProgress> findAllByUserAndBadge(User user, Badge badge);
}
