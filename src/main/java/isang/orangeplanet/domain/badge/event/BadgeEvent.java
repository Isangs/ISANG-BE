package isang.orangeplanet.domain.badge.event;

import isang.orangeplanet.global.utils.enums.Badge;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class BadgeEvent {
  Badge badge;
}
