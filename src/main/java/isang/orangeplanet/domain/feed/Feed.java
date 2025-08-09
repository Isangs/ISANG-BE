package isang.orangeplanet.domain.feed;

import isang.orangeplanet.domain.feed.controller.dto.FeedDto;
import isang.orangeplanet.domain.task.Task;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
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
  private Long hearts;

  @Column(name = "likes", nullable = false)
  private Long likes;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "task_id", unique = true)
  private Task task;

  @Column(name = "content")
  private String content;

  @Column(name = "image_url")
  private String imageUrl;

  public FeedDto toDto(){
    return FeedDto.builder()
        .id(feedId)
        .createdAt(getCreatedAt())
        .hearts(hearts)
        .likes(likes)
        .profileImageUrl(imageUrl)
        .content(content)
        .isPublic(task.getIsPublic())
        .user(task.getUser().toSimpleDto())
        .taskMessage(task.getName())
        .build();
  }
}
