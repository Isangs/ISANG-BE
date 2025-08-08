package isang.orangeplanet.domain.user.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import isang.orangeplanet.global.utils.enums.Role;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserSimpleDto {
  private String profileImageUrl;
  private String name;
}
