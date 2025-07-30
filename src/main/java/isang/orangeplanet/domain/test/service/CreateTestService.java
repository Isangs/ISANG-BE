package isang.orangeplanet.domain.test.service;

import isang.orangeplanet.domain.test.Test;
import isang.orangeplanet.domain.test.controller.request.CreateTestRequest;
import isang.orangeplanet.domain.test.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateTestService {

  private final TestRepository testRepository;

  /**
   * 테스트 생성
   */
  public void createTest(CreateTestRequest request) {
    this.testRepository.save(
      Test.builder()
        .title(request.title())
        .content(request.content())
        .build()
    );
  }
}
