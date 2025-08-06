package isang.orangeplanet.domain.feed;

import isang.orangeplanet.domain.task.Task;
import isang.orangeplanet.domain.user.User;
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

  @Column(name = "hearts", nullable = false)
  private Long heart;

  @Column(name = "likes", nullable = false)
  private Long like;

  @JoinColumn(name = "user_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @JoinColumn(name = "task_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Task task;

  @Column(name = "content")
  private String content;

  @Column(name = "image_url")
  private String imageUrl;
}
