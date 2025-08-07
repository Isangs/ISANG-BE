package isang.orangeplanet.domain.goal.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import isang.orangeplanet.domain.goal.controller.response.*;
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

  /**
   * 주간 완료된 할일 목록 조회
   * 아오 힘들어 오랜만에 쿼리 짜니까 머리 아프네..ㅎㅎ
   * @param userId : 회원 ID
   * @return : ListWeeklyAchievementDto 객체 반환
   */
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
          task.goal.goalId.as("goalId"),
          task.goal.name.as("name"),
          weekdayName.as("day"), // 요일
          task.count().multiply(100).as("score"), // 완료된 점수

          // 목표별 최대 점수
          ExpressionUtils.as(
            JPAExpressions.select(task.count().multiply(100))
              .from(task)
              .where(
                task.goal.goalId.eq(goal.goalId)
                  .and(task.user.userId.eq(userId))
              ),
            "maxScore"
          )
        )
      )
      .from(task)
      .leftJoin(feed).on(
        feed.task.eq(task)
          .and(feed.createdAt.between(
            startOfWeek.atStartOfDay(),
            endOfWeek.plusDays(1).atStartOfDay().minusNanos(1)
          ))
      )
      .leftJoin(goal).on(task.goal.eq(goal))
      .where(
        task.user.userId.eq(userId)
          .and(task.isCompleted.eq(true))
      )
      .groupBy(task.goal.goalId, task.goal.name, weekdayName)
      .fetch();
  }
}
