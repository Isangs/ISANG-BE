package isang.orangeplanet.domain.feed.repository;

import isang.orangeplanet.domain.feed.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    @Query("SELECT f FROM Feed f " +
        "WHERE f.content LIKE %:query% " +
        "OR f.user.name LIKE %:query% " +
        "OR f.task.name LIKE %:query%")
    List<Feed> findByUserNameLikeOrContentLikeOrTaskNameLike(@Param("query") String query);
}
