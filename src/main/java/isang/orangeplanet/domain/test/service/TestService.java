package isang.orangeplanet.domain.test.service;

import isang.orangeplanet.domain.test.controller.response.DetailTestResponse;
import isang.orangeplanet.domain.test.controller.response.ListTestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 인터페이스 대체용 class입니다.
 * 생성/수정/삭제 관련 서비스 클래스, 상세 조회 서비스 클래스, 목록 조회 서비스 클래스를
 * 한 곳에서 편하게 관리하기 좋을 것 같아 해커톤에 적합해보여 선택했습니다.
 */
@Service
@RequiredArgsConstructor
public class TestService {

  private final CreateTestService createTestService;
  private final DetailTestService detailTestService;
  private final ListTestService listTestService;

  public void createTest(CreateTestService request) {
    createTestService.createTest(request);
  }

  public DetailTestResponse getTest(Long testId) {
    return detailTestService.getTest(testId);
  }

  public ListTestResponse getTestList() {
    return listTestService.getTestList();
  }
}
