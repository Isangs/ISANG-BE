package isang.orangeplanet.domain.goal.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import isang.orangeplanet.domain.goal.controller.response.GetGoalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.types.ExpressionUtils.as;
import static isang.orangeplanet.domain.goal.QGoal.goal;
import static isang.orangeplanet.domain.task.QTask.task;

@Repository
@RequiredArgsConstructor
public class GoalRepository {
  private final JPAQueryFactory queryFactory;

  public List<GetGoalDto> listDetailGoal(String userId) {
    return this.queryFactory.select(
      Projections.fields(
        GetGoalDto.class,
        goal.goalId.as("goalId"),
        as(task.score.sum(), "score"),
        task.maxScore.as("maxScore"),
        goal.colorCode.as("colorCode"),
        goal.name
      )
    )
    .from(goal)
    .leftJoin(task).on(task.goal.eq(goal))
    .where(task.user.userId.eq(userId))
    .groupBy(goal.goalId, task.maxScore, goal.colorCode, goal.name)
    .fetch();
  }
}
