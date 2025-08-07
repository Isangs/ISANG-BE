package isang.orangeplanet.domain.feed.repository;

import isang.orangeplanet.domain.feed.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findByUserNameLikeOrContentLikeOrTaskNameLike(String userName, String content, String taskName);
}
