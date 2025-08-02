package isang.orangeplanet.domain.record;

import isang.orangeplanet.domain.feed.Feed;
import isang.orangeplanet.domain.task.Task;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.global.config.jpa.BooleanToYNConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "record")
public class Record {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "record_id", nullable = false)
  private Long recordId;

  @Column(name = "text")
  private String text;

  @Column(name = "file_url")
  private String fileUrl;

  @Column(name = "score")
  private Long score;

  @Column(name = "day", nullable = false)
  private String day;

  @Column(name = "is_public", columnDefinition = "char(1) default 'N'")
  @Convert(converter = BooleanToYNConverter.class)
  private Boolean isPublic;

  @Column(name = "completed_time", nullable = false)
  private LocalDateTime completedTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "task_id")
  private Task task;

  @Builder.Default
  @OneToMany(mappedBy = "record", cascade = CascadeType.ALL)
  private List<Feed> feedList = new ArrayList<>();
}
