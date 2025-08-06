package isang.orangeplanet.domain.goal.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import isang.orangeplanet.domain.goal.controller.response.GetGoalDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static isang.orangeplanet.domain.goal.QGoal.goal;
import static isang.orangeplanet.domain.task.QTask.task;

@Repository
@RequiredArgsConstructor
public class GoalRepository {
  private final JPAQueryFactory queryFactory;

  public List<GetGoalDto> listDetailGoal(String userId) {
    NumberExpression<Integer> completedScore = new CaseBuilder()
        .when(task.isCompleted.isTrue()).then(1)
        .otherwise(0)
        .sum()
        .multiply(100);

    return this.queryFactory.select(
      Projections.fields(
        GetGoalDto.class,
        goal.goalId.as("goalId"),
        completedScore.as("score"),
        task.count().multiply(100).as("maxScore"),
        goal.colorCode.as("colorCode"),
        goal.name
      )
    )
    .from(goal)
    .leftJoin(task).on(task.goal.eq(goal))
    .where(task.user.userId.eq(userId))
    .groupBy(goal.goalId, goal.colorCode, goal.name)
    .fetch();
  }
}
