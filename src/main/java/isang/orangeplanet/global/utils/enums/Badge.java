package isang.orangeplanet.global.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Badge {

  THREE_DAY("3일 연속", "3일 연속 할일 완료", 3),
  OVER_PERFECT_SCORES("100점 돌파", "총 점수 100점 달성", 100),
  PERFECT_WEEK("완벽한 주", "일주일 모든 할일 완료", 7),
  BEGINNER_ESCAPE("초보 달출", "레벨 10 달성", 10),
  MONTHLY_KING("월간 왕", "한 달간 1위 유지", 30),
  MASTER("마스터", "레벨 50 달성", 50);

  private final String name;
  private final String desc;
  private final int condition;
}
