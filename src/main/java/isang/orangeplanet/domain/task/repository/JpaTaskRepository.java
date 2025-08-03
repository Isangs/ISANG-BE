package isang.orangeplanet.domain.task.repository;

import isang.orangeplanet.domain.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTaskRepository extends JpaRepository<Task, Long> {
}
