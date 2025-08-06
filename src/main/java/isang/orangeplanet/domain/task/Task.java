package isang.orangeplanet.domain.task;

import isang.orangeplanet.domain.goal.Goal;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "goal_id")
  private Goal goal;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
  private Boolean isCompleted;

  private Boolean isAddFeed;

  private Boolean isPublic;

  public void updateVisibility(Boolean isAddFeed, Boolean isPublic) {
    this.isAddFeed = isAddFeed;
    this.isPublic = isPublic;
  }

  public void updateIsCompleted(Boolean isCompleted) {
    this.isCompleted = isCompleted;
  }
}
