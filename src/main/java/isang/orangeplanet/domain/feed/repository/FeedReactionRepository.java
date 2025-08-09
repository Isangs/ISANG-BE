package isang.orangeplanet.domain.feed.repository;

import isang.orangeplanet.domain.feed.Feed;
import isang.orangeplanet.domain.feed.FeedReaction;
import isang.orangeplanet.domain.feed.enums.ReactionType;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.domain.user.utils.UserUtils;
import org.apache.poi.sl.draw.geom.GuideIf;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedReactionRepository extends JpaRepository<FeedReaction, Long> {
    List<FeedReaction> findByUserAndReactionType(User user, ReactionType reactionType);

    Optional<FeedReaction> findByFeedAndUserAndReactionType(Feed feed, User user, ReactionType reactionType);
}
