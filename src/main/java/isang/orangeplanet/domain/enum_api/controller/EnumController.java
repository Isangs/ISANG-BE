package isang.orangeplanet.domain.enum_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import isang.orangeplanet.domain.enum_api.controller.response.ListBadgeEnumResponse;
import isang.orangeplanet.domain.enum_api.controller.response.ListPriorityEnumResponse;
import isang.orangeplanet.domain.enum_api.service.EnumService;
import isang.orangeplanet.global.api_response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * EnumController : Enum 관련 Controller
 */
@RestController
@RequestMapping(value = "/enum")
@RequiredArgsConstructor
@Tag(name = "Enum(선택형 필드)", description = "Enum 관련 API")
public class EnumController {
  private final EnumService enumService;

  /**
   * 뱃지 목록 조회
   * @return : 뱃지 Enum 목록 반환
   */
  @GetMapping(value = "/badge")
  @Operation(summary = "뱃지", description = "뱃지 Enum 목록 조회")
  public ApiResponse<ListBadgeEnumResponse> getBadgeEnumList() {
    return ApiResponse.onSuccess(enumService.getBadgeEnumList());
  }

  /**
   * 우선 순위 목록 조회
   * @return : 우선 순위 Enum 목록 반환
   */
  @GetMapping(value = "/priority")
  @Operation(summary = "우선 순위", description = "우선 순위 Enum 목록 조회")
  public ApiResponse<ListPriorityEnumResponse> getPriorityEnumList() {
    return ApiResponse.onSuccess(enumService.getPriorityEnumList());
  }
}
