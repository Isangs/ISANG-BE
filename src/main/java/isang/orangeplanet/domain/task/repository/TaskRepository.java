package isang.orangeplanet.domain.task.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import isang.orangeplanet.domain.task.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static isang.orangeplanet.domain.task.QTask.task;

@Repository
@RequiredArgsConstructor
public class TaskRepository {
  private final JPAQueryFactory queryFactory;

  /**
   * 할일 목록 조회
   * @param userId : 회원 ID
   * @return : 우선순위를 기준으로 정렬된 할일 목록 반환
   */
  public List<Task> findTaskByUserIdAndIsCompleted(String userId, Boolean isCompleted) {
    return this.queryFactory.selectFrom(task)
      .where(
          task.user.userId.eq(userId).and(
              task.isCompleted.eq(false)
          )
      )
      .orderBy(task.priority.desc())
      .fetch();
  }

  public List<Task> taskListByGoalId(long goalId, String userId) {
    return this.queryFactory.selectFrom(task)
      .where(
        task.user.userId.eq(userId),
        task.goal.goalId.eq(goalId)
      )
      .orderBy(task.priority.desc())
      .fetch();
  }
}
