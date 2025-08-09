package isang.orangeplanet.domain.activity.entity;

import isang.orangeplanet.domain.feed.Feed;
import isang.orangeplanet.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.repository.EntityGraph;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
    attributeNodes = {
        @NamedAttributeNode(value = "feed", subgraph = "feedWithTask"),
    },
    subgraphs = {
        @NamedSubgraph(
            name = "feedWithTask",
            attributeNodes = {
                @NamedAttributeNode(value = "task", subgraph = "taskWithUser")
            }
        ),
        @NamedSubgraph(
            name = "taskWithUser",
            attributeNodes = {
                @NamedAttributeNode(value = "user")
            }
        )
    }
)
public class Activity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", unique = true)
    private Feed feed;

    public void updateFeed(Feed feed) {
        this.feed = feed;
    }
}
