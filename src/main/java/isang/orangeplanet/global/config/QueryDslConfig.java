package isang.orangeplanet.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * QueryDslConfig : QueryDsl 설정 파일
 */
@Configuration
@AllArgsConstructor
public class QueryDslConfig {
  private final EntityManager em;

  @Bean
  public JPAQueryFactory queryFactory() {
    return new JPAQueryFactory(em);
  }
}
