package isang.orangeplanet.domain.goal;

import isang.orangeplanet.domain.task.Task;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "goal")
public class Goal extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "goal_id", nullable = false)
  private Long goalId;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "color_code", nullable = false)
  private String colorCode;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Builder.Default
  @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL)
  private List<Task> taskList = new ArrayList<>();
}
