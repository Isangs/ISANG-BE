package isang.orangeplanet.domain.user.controller.dto;

import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserSimpleDto {
  private String profileImageUrl;
  private String name;
}
