package isang.orangeplanet.domain.feed.repository;

import isang.orangeplanet.domain.feed.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
}
