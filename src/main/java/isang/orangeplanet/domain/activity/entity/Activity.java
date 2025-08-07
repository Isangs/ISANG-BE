package isang.orangeplanet.domain.activity.entity;

import isang.orangeplanet.domain.feed.Feed;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Activity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", unique = true)
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void updateFeed(Feed feed) {
        this.feed = feed;
    }
}
