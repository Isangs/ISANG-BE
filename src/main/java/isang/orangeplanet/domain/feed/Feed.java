package isang.orangeplanet.domain.feed;

import isang.orangeplanet.domain.record.Record;
import isang.orangeplanet.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "feed")
public class Feed extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "feed_id", nullable = false)
  private Long feedId;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "hearts", nullable = false)
  private Long heart;

  @Column(name = "likes", nullable = false)
  private Long like;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "record_id")
  private Record record;
}
