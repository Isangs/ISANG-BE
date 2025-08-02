package isang.orangeplanet.domain.badge;

import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.global.config.jpa.BooleanToYNConverter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "badge")
public class Badge {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "badge_id", nullable = false)
  private Long badgeId;

  @Enumerated(EnumType.STRING)
  @Column(name = "badge")
  private isang.orangeplanet.global.utils.enums.Badge badge;

  @Column(name = "is_achieved", nullable = false, columnDefinition = "char(1) default 'N'")
  @Convert(converter = BooleanToYNConverter.class)
  private Boolean isAchieved;

  @Column(name = "progress", nullable = false)
  private Long progress;

  @Column(name = "day_count", nullable = false)
  private Long dayCount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;
}
