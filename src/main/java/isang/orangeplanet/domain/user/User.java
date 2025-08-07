package isang.orangeplanet.domain.user;

import isang.orangeplanet.domain.badge.Badge;
import isang.orangeplanet.domain.goal.Goal;
import isang.orangeplanet.domain.task.Task;
import isang.orangeplanet.domain.user.controller.dto.UserSimpleDto;
import isang.orangeplanet.domain.user.service.UserService;
import isang.orangeplanet.global.config.jpa.CryptoConverter;
import isang.orangeplanet.global.domain.BaseTimeEntity;
import isang.orangeplanet.global.utils.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User extends BaseTimeEntity {

  @Id
  @Column(name = "user_id", nullable = false)
  private String userId;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "nick_name")
  private String nickName;

  @Column(name = "profile_url", nullable = false)
  private String profileUrl;

  @Column(name = "email", nullable = false)
  @Convert(converter = CryptoConverter.class)
  private String email;

  @Column(name = "introduce")
  private String introduce;

  @Column(name = "total_score")
  private Long totalScore;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  private Role role;

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Goal> goalList = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Task> taskList = new ArrayList<>();

  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Badge> badgeList = new ArrayList<>();

  public void sumTotalScore(Long score) {
    this.totalScore += score;
  }

  public UserSimpleDto toSimpleDto(){
    return UserSimpleDto.builder()
        .name(name)
        .profileImageUrl(profileUrl)
        .build();
  }
}
