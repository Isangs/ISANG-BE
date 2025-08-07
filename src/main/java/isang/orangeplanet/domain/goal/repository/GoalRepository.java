package isang.orangeplanet.domain.goal.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import isang.orangeplanet.domain.goal.controller.response.GetGoalDto;
import isang.orangeplanet.domain.goal.controller.response.ListDayOfWeekDto;
import isang.orangeplanet.domain.goal.controller.response.ListWeeklyAchievementDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static isang.orangeplanet.domain.feed.QFeed.feed;
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

  public List<ListDayOfWeekDto> daysOfWeek(String userId) {
    StringTemplate weekdayName = Expressions.stringTemplate(
      "DATE_FORMAT({0}, '%W')", task.createdAt
    );

    LocalDate today = LocalDate.now();
    int daysFromMonday = today.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();

    LocalDate startOfWeek = today.minusDays(daysFromMonday); // 월요일
    LocalDate endOfWeek = startOfWeek.plusDays(6); // 일요일

    return this.queryFactory
      .select(
        Projections.fields(
          ListDayOfWeekDto.class,
          weekdayName.as("day"),
          task.count().multiply(100).as("score")
        )
      )
      .from(task)
      .where(
        task.user.userId.eq(userId),
        task.createdAt.between(
          startOfWeek.atStartOfDay(),
          endOfWeek.plusDays(1).atStartOfDay().minusNanos(1)
        )
      )
      .groupBy(weekdayName)
      .fetch();
  }


  public List<ListWeeklyAchievementDto> weeklyAchievement(String userId) {
    StringTemplate weekdayName = Expressions.stringTemplate(
      "DATE_FORMAT({0}, '%W')", feed.createdAt
    );

    LocalDate today = LocalDate.now();
    int daysFromMonday = today.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();

    LocalDate startOfWeek = today.minusDays(daysFromMonday); // 월요일
    LocalDate endOfWeek = startOfWeek.plusDays(6); // 일요일

    return this.queryFactory.select(
      Projections.fields(
        ListWeeklyAchievementDto.class,
        task.goal.goalId,
        task.goal.name,
        weekdayName.as("day"),
        task.count().multiply(100).as("score"),

        ExpressionUtils.as( // 목표별 최대 점수
          JPAExpressions.select(task.count().multiply(100))
          .from(task)
          .where(
            task.goal.goalId.eq(goal.goalId) // 상관 쿼리 사용
              .and(task.user.userId.eq(userId))
          )
          , "maxScore"
        )
      )
    )
    .from(task)
    .leftJoin(feed)
    .on(feed.task.eq(task)
      .and( // 일주일
        feed.createdAt.between(
          startOfWeek.atStartOfDay(),
          endOfWeek.plusDays(1).atStartOfDay().minusNanos(1)
        )
      )
    )
    .leftJoin(goal).on(task.goal.eq(goal))
    .where(
      task.user.userId.eq(userId)
        .and(task.isCompleted.eq(true))
    )
    .groupBy(weekdayName, task.goal.goalId, task.goal.name)
    .fetch();
  }
}
