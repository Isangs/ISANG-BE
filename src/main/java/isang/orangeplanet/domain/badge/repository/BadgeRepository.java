package isang.orangeplanet.domain.badge.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BadgeRepository {
  private final JPAQueryFactory queryFactory;


}
