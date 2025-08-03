package isang.orangeplanet.domain.goal.repository;

import isang.orangeplanet.domain.goal.Goal;
import isang.orangeplanet.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaGoalRepository extends JpaRepository<Goal, Long> {
  Goal findGoalByName(String name);
  Optional<List<Goal>> findGoalByUser(User user);
}
