package isang.orangeplanet.domain.test.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import isang.orangeplanet.domain.test.controller.request.CreateTestRequest;
import isang.orangeplanet.domain.test.controller.response.DetailTestResponse;
import isang.orangeplanet.domain.test.controller.response.ListTestResponse;
import isang.orangeplanet.domain.test.service.TestService;
import isang.orangeplanet.global.api_response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * test 패키지는 컨벤션 문서용 패키지입니다.
 * 최소한의 컨벤션은 필요한데, 따로 문서화하기엔 시간이 너무 걸릴 것 같아 주석으로 대체합니다.
 *
 * 추가로, 해당 컨벤션 내용은 혹시나 개발을 헤매실까봐 전부 참고용으로 적는 것입니다!
 * 본인만의 편한 방식이 있다면, 그대로 개발하셔도 당연히 당연히 좋습니다.
 */

/**
 * Controller 애노테이션은 아래 네 개로 고정하겠습니다.
 * | @RequestMapping, @Tag 는 controller별로 수정해주세요!
 * ex) @RequestMapping("/user"), @Tag(name = "User", description = "회원 관련 API")
 */

@Hidden
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Tag(name = "Test", description = "테스트 관련 API")
public class TestController {
  private final TestService testService;

  /**
   * Controller부터 Repository까지 연결된 메서드는 이름을 통일해주세요!
   * 앞으로 유지보수하기 편할 것 같습니다.
   *
   * 요청 Dto, 응답 Dto도 이름 형식을 맞춰주세요.
   * 생성 관련 : CreateXXXRequest
   * 상세 조회 관련 : getXXX
   * 목록 조회 관련 : getXXXList
   */
  @PostMapping
  @Operation(summary = "테스트 생성")
  public ApiResponse<Void> createTest(@RequestBody @Valid CreateTestRequest request) {
    testService.createTest(request);
    return ApiResponse.onSuccess();
  }

  @GetMapping("/{testId}")
  @Operation(summary = "테스트 상세 조회")
  public ApiResponse<DetailTestResponse> getTest(
    /*
     * @PathVariable 뒤 ("")에 명시를 안 해주면 swagger가 인식을 못 합니다!
     */
    @PathVariable("testId") Long testId
  ) {
    return ApiResponse.onSuccess(testService.getTest(testId));
  }

  @GetMapping
  @Operation(summary = "테스트 목록 조회")
  public ApiResponse<ListTestResponse> getTestList() {
    return ApiResponse.onSuccess(testService.getTestList());
  }
}
