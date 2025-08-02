package isang.orangeplanet.domain.badge.repository;

import isang.orangeplanet.domain.badge.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
}
