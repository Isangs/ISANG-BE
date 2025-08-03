package isang.orangeplanet.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import isang.orangeplanet.domain.user.controller.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static isang.orangeplanet.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class UpdateUserRepository {
  private final JPAQueryFactory queryFactory;

  public void update(UpdateUserRequest request) {
    queryFactory.update(user)
      .set(user.name, request.name())
      .set(user.nickName, request.nickName())
      .set(user.profileUrl, request.profileUrl())
      .set(user.email, request.email())
      .set(user.introduce, request.introduce())
      .execute();
  }
}
