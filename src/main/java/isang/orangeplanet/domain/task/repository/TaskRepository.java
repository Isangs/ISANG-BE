package isang.orangeplanet.domain.task.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import isang.orangeplanet.domain.task.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

  public List<Task> findByGoalId(String userId, Long goalId) {
    return this.queryFactory.selectFrom(task)
      .where(
        task.user.userId.eq(userId)
          .and(task.goal.goalId.eq(goalId))
      )
      .fetch();
  }

  /**
   * 일주일동안 생성된 할일중 완료되지 않은것이 있는지 확인
   * @param userId : 회원 ID
   * @return : 완료 여부
   */
  public boolean isPerfectWeek(String userId, LocalDateTime startOfWeek, LocalDateTime endOfWeek) {
    Long uncompletedCount = queryFactory
      .select(task.count())
      .from(task)
      .where(
        task.user.userId.eq(userId),
        task.createdAt.between(startOfWeek, endOfWeek),
        task.isCompleted.isFalse()
      )
      .fetchOne();

    return uncompletedCount != null && uncompletedCount == 0;
  }
}
