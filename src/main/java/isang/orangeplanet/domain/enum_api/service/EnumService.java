package isang.orangeplanet.domain.enum_api.service;

import isang.orangeplanet.domain.enum_api.controller.response.ListEnumDto;
import isang.orangeplanet.domain.enum_api.controller.response.ListEnumResponse;
import isang.orangeplanet.global.utils.enums.Badge;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

/**
 * EnumService : Enum 관련 Service
 */
@Service
public class EnumService {

  /**
   * Badge Enum 목록 조회
   * @return : Badge Enum 응답 객체 반환
   */
  public ListEnumResponse getBadgeEnumList() {
    List<ListEnumDto> enumList = Stream.of(Badge.values())
      .map(e ->
        ListEnumDto.builder()
          .code(e.name())
          .badge(e.getName())
          .desc(e.getDesc())
          .condition(e.getCondition())
          .build()
      ).toList();

    return ListEnumResponse.builder()
      .badgeList(enumList)
      .build();
  }
}
