package isang.orangeplanet.domain.activity.repository;


import isang.orangeplanet.domain.activity.entity.Activity;
import isang.orangeplanet.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityJpaRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByUser(User user);

    List<Activity> findByUser(User user, Pageable pageable);
}
