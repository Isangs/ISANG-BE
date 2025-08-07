package isang.orangeplanet.domain.activity.repository;


import isang.orangeplanet.domain.activity.entity.Activity;
import isang.orangeplanet.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityJpaRepository extends JpaRepository<Activity, Long> {
    @EntityGraph(attributePaths = { "feed", "feed.task", "feed.user" })
    List<Activity> findByFeedUser(User user);

    @EntityGraph(attributePaths = { "feed", "feed.task", "feed.user" })
    List<Activity> findByFeedUser(User user, Pageable pageable);
}
