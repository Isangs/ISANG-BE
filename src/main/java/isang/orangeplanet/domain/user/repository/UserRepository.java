package isang.orangeplanet.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.domain.user.controller.dto.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static isang.orangeplanet.domain.feed.QFeed.feed;
import static isang.orangeplanet.domain.task.QTask.task;
import static isang.orangeplanet.domain.user.QUser.user;

/**
 * UserRepository : 회원 관련 QueryDSL Repository
 */
@Repository
@RequiredArgsConstructor
public class UserRepository {
  private final JPAQueryFactory queryFactory;

  /**
   * 회원 정보 수정
   * @param request : UpdateUserRequest 객체
   */
  public void update(UpdateUserRequest request) {
    queryFactory.update(user)
      .set(user.name, request.name())
      .set(user.nickName, request.nickName())
      .set(user.profileUrl, request.profileUrl())
      .set(user.email, request.email())
      .set(user.introduce, request.introduce())
      .execute();
  }

  public User getTopRankerForToday(LocalDateTime startOfDay, LocalDateTime endOfDate) {
    return queryFactory
      .select(task.user)
      .from(task)
      .leftJoin(feed).on(feed.createdAt.between(startOfDay, endOfDate))
      .where(task.isCompleted.isTrue())
      .groupBy(task.user)
      .orderBy(user.totalScore.desc())
      .limit(1)
      .fetchOne();
  }
}
