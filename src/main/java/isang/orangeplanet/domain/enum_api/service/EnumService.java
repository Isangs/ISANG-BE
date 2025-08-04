package isang.orangeplanet.domain.enum_api.service;

import isang.orangeplanet.domain.enum_api.controller.response.ListBadgeEnumDto;
import isang.orangeplanet.domain.enum_api.controller.response.ListPriorityEnumDto;
import isang.orangeplanet.domain.enum_api.controller.response.ListBadgeEnumResponse;
import isang.orangeplanet.domain.enum_api.controller.response.ListPriorityEnumResponse;
import isang.orangeplanet.global.utils.enums.Badge;
import isang.orangeplanet.global.utils.enums.Priority;
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
  public ListBadgeEnumResponse getBadgeEnumList() {
    List<ListBadgeEnumDto> enumList = Stream.of(Badge.values())
      .map(e ->
        ListBadgeEnumDto.builder()
          .code(e.name())
          .badge(e.getName())
          .desc(e.getDesc())
          .condition(e.getCondition())
          .build()
      ).toList();

    return ListBadgeEnumResponse.builder()
      .badgeList(enumList)
      .build();
  }

  /**
   * Priority Enum 목록 조회
   * @return : Priority Enum 응답 객체 반환
   */
  public ListPriorityEnumResponse getPriorityEnumList() {
    List<ListPriorityEnumDto> enumList = Stream.of(Priority.values())
      .map(e ->
        ListPriorityEnumDto.builder()
          .code(e.name())
          .priority(e.getValue())
          .score(e.getScore())
          .build()
      ).toList();

    return ListPriorityEnumResponse.builder()
      .priorityList(enumList)
      .build();
  }
}
