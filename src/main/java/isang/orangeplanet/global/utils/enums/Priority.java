package isang.orangeplanet.global.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Priority {

  HIGH("높음", 90),
  NORMAL("보통", 50),
  LOW("낮음", 20);

  private final String value;
  private final int score;
}
