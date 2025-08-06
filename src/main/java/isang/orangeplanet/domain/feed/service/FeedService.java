package isang.orangeplanet.domain.feed.service;

import isang.orangeplanet.domain.auth.utils.SecurityUtils;
import isang.orangeplanet.domain.feed.Feed;
import isang.orangeplanet.domain.feed.controller.dto.request.CompleteTaskWithImageRequest;
import isang.orangeplanet.domain.feed.repository.FeedRepository;
import isang.orangeplanet.domain.feed.controller.dto.request.CompleteTaskWithTextRequest;
import isang.orangeplanet.domain.task.Task;
import isang.orangeplanet.domain.task.repository.JpaTaskRepository;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.domain.user.utils.UserUtils;
import isang.orangeplanet.global.api_response.exception.GeneralException;
import isang.orangeplanet.global.api_response.status.ErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FeedService {
  private final JpaTaskRepository jpaTaskRepository;
  private final FeedRepository feedRepository;

  public void completeTaskWithText(Long taskId, CompleteTaskWithTextRequest request){
    User currentUser = UserUtils.getUser(SecurityUtils.getAuthUserId());
    Task task = jpaTaskRepository.findById(taskId).orElseThrow(() ->
      new GeneralException(ErrorStatus.BAD_REQUEST, "해당하는 할일을 찾을 수 없습니다.")
    );

    if(task.getUser() != currentUser) {
      throw new GeneralException(ErrorStatus.BAD_REQUEST, "자신의 할일만 변경할 수 있습니다.");
    }

    task.updateIsCompleted(true);

    if(task.getIsAddFeed()) {
      Feed newFeed = Feed.builder()
          .like(0L)
          .heart(0L)
          .content(request.getContent())
          .task(task)
          .user(currentUser)
          .build();

      feedRepository.save(newFeed);
    }
  }

  public void completeTaskWithImage(Long taskId, CompleteTaskWithImageRequest request){
    User currentUser = UserUtils.getUser(SecurityUtils.getAuthUserId());
    Task task = jpaTaskRepository.findById(taskId).orElseThrow(() ->
        new GeneralException(ErrorStatus.BAD_REQUEST, "해당하는 할일을 찾을 수 없습니다.")
    );

    if(task.getUser() != currentUser) {
      throw new GeneralException(ErrorStatus.BAD_REQUEST, "자신의 할일만 변경할 수 있습니다.");
    }

    task.updateIsCompleted(true);

    if(task.getIsAddFeed()) {
      Feed newFeed = Feed.builder()
          .imageUrl(request.getImageUrl())
          .like(0L)
          .heart(0L)
          .task(task)
          .user(currentUser)
          .build();

      feedRepository.save(newFeed);
    }
  }
}
