package isang.orangeplanet.domain.task;

import isang.orangeplanet.domain.goal.Goal;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "task")
public class Task extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "task_id", nullable = false)
  private Long taskId;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "priority", nullable = false)
  private Integer priority;

  @Column(name = "deadline", nullable = false)
  private LocalDateTime deadline;

  @JoinColumn(name = "goal_id")
  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Goal goal;

  @JoinColumn(name = "user_id")
  @ManyToOne(fetch = FetchType.LAZY)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private User user;

  @Column(name = "is_completed")
  private Boolean isCompleted;

  @Column(name = "is_add_feed")
  private Boolean isAddFeed;

  @Column(name = "is_public")
  private Boolean isPublic;

  public void updateVisibility(Boolean isAddFeed, Boolean isPublic) {
    this.isAddFeed = isAddFeed;
    this.isPublic = isPublic;
  }

  public void updateIsCompleted(Boolean isCompleted) {
    this.isCompleted = isCompleted;
  }
}
