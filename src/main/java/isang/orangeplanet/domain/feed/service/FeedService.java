package isang.orangeplanet.domain.feed.service;

import isang.orangeplanet.domain.activity.entity.Activity;
import isang.orangeplanet.domain.activity.repository.ActivityJpaRepository;
import isang.orangeplanet.domain.auth.utils.SecurityUtils;
import isang.orangeplanet.domain.feed.Feed;
import isang.orangeplanet.domain.feed.controller.dto.FeedDto;
import isang.orangeplanet.domain.feed.controller.dto.request.CompleteTaskWithImageRequest;
import isang.orangeplanet.domain.feed.controller.dto.response.FetchFeedListResponse;
import isang.orangeplanet.domain.feed.controller.dto.response.SearchFeedListResponse;
import isang.orangeplanet.domain.feed.repository.FeedRepository;
import isang.orangeplanet.domain.feed.controller.dto.request.CompleteTaskWithTextRequest;
import isang.orangeplanet.domain.task.Task;
import isang.orangeplanet.domain.task.repository.JpaTaskRepository;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.domain.user.controller.dto.UserSimpleDto;
import isang.orangeplanet.domain.user.utils.UserUtils;
import isang.orangeplanet.global.api_response.exception.GeneralException;
import isang.orangeplanet.global.api_response.status.ErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FeedService {
  private final JpaTaskRepository jpaTaskRepository;
  private final FeedRepository feedRepository;
  private final ActivityJpaRepository activityJpaRepository;

  public SearchFeedListResponse searchFeedList(String query) {
    List<Feed> feeds = feedRepository.findByUserNameLikeOrContentLikeOrTaskNameLike(query);
    List<FeedDto> responses = feeds.stream().map(Feed::toDto).toList();

    return new SearchFeedListResponse(responses);
  }

  public FetchFeedListResponse fetchFeedList() {
    List<Feed> feeds = feedRepository.findAll();
    List<FeedDto> responses = feeds.stream().map(Feed::toDto).toList();

    return new FetchFeedListResponse(responses);
  }

  public void completeTaskWithText(Long taskId, CompleteTaskWithTextRequest request){
    User currentUser = UserUtils.getUser(SecurityUtils.getAuthUserId());
    Feed newFeed = completeTask(currentUser, taskId, null, request.getContent());

    Activity newActivity = Activity.builder()
        .user(currentUser)
        .feed(newFeed)
        .build();

    activityJpaRepository.save(newActivity);
  }

  public void completeTaskWithImage(Long taskId, CompleteTaskWithImageRequest request){
    User currentUser = UserUtils.getUser(SecurityUtils.getAuthUserId());
    Feed newFeed = completeTask(currentUser, taskId, request.getImageUrl(), null);

    Activity newActivity = Activity.builder()
        .user(currentUser)
        .feed(newFeed)
        .build();

    activityJpaRepository.save(newActivity);
  }

  private Feed completeTask(User user, Long taskId, String imageUrl, String content){
    Task task = jpaTaskRepository.findById(taskId).orElseThrow(() ->
        new GeneralException(ErrorStatus.BAD_REQUEST, "해당하는 할일을 찾을 수 없습니다.")
    );

    if(task.getIsCompleted()) {
      throw new GeneralException(ErrorStatus.BAD_REQUEST, "이미 할일이 마무리되었습니다.");
    }

    if(!task.getUser().equals(user)) {
      throw new GeneralException(ErrorStatus.BAD_REQUEST, "자신의 할일만 변경할 수 있습니다.");
    }

    task.updateIsCompleted(true);
    user.sumTotalScore(100L);

    Feed newFeed = null;
    if(task.getIsAddFeed()) {
      newFeed = Feed.builder()
          .likes(0L)
          .hearts(0L)
          .task(task)
          .content(content)
          .imageUrl(imageUrl)
          .user(user)
          .build();

      feedRepository.save(newFeed);
    }

    return newFeed;
  }
}
