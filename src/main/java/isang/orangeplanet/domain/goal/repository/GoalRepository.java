package isang.orangeplanet.domain.goal.repository;

import isang.orangeplanet.domain.goal.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
}
