package isang.orangeplanet.domain.badge;

import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.global.domain.BaseTimeEntity;
import isang.orangeplanet.global.utils.enums.Badge;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "badge_progress")
public class BadgeProgress extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "badge")
  private Badge badge;

  @Column(name = "progress")
  private int progress;

  @Column(name = "is_achieved")
  private boolean isAchieved;

  @Column(name = "last_completed_at")
  private LocalDateTime lastCompletedAt;

  @Column(name = "last_maintained_at")
  private LocalDate lastMaintainedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;


  public void updateProgress(int progress) {
    this.progress = progress;
  }

  public void updateIsAchieved(boolean isAchieved) {
    this.isAchieved = isAchieved;
  }

  public void updateLastCompletedAt(LocalDateTime date) {
    this.lastCompletedAt = date;
  }

  public void updateLastMaintainedAt(LocalDate date) {
    this.lastMaintainedAt = date;
  }
}

