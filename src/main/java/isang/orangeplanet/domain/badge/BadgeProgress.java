package isang.orangeplanet.domain.badge;

import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.global.utils.enums.Badge;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "badge_progress")
public class BadgeProgress {

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;


  public void updateProgress(int progress) {
    this.progress = progress;
  }

  public void updateIsAchieved(boolean isAchieved) {
    this.isAchieved = isAchieved;
  }
}

