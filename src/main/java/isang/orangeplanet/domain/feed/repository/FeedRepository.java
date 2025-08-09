package isang.orangeplanet.domain.feed.repository;

import isang.orangeplanet.domain.feed.Feed;
import isang.orangeplanet.domain.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    @Query("SELECT f FROM Feed f " +
        "WHERE f.content LIKE %:query% " +
        "OR f.task.user.name LIKE %:query% " +
        "OR f.task.name LIKE %:query%")
    List<Feed> findByUserNameLikeOrContentLikeOrTaskNameLike(@Param("query") String query);

    @EntityGraph(attributePaths = { "task", "task.user" })
    List<Feed> findByTaskUser(User user);
}
