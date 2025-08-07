package isang.orangeplanet.domain.feed.service;

import isang.orangeplanet.domain.auth.utils.SecurityUtils;
import isang.orangeplanet.domain.badge.event.BadgeEvent;
import isang.orangeplanet.domain.badge.service.BadgeService;
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
import isang.orangeplanet.domain.user.repository.UserRepository;
import isang.orangeplanet.domain.user.utils.UserUtils;
import isang.orangeplanet.global.api_response.exception.GeneralException;
import isang.orangeplanet.global.api_response.status.ErrorStatus;
import isang.orangeplanet.global.utils.enums.Badge;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FeedService {
  private final ApplicationEventPublisher eventPublisher;
  private final JpaTaskRepository jpaTaskRepository;
  private final FeedRepository feedRepository;

  private final UserRepository userRepository;
  private final BadgeService badgeService;

  public SearchFeedListResponse searchFeedList(String query) {
    List<Feed> feeds = feedRepository.findByUserNameLikeOrContentLikeOrTaskNameLike(query);

    List<FeedDto> responses = feeds.stream().map(feed -> {
      User user = feed.getUser();
      UserSimpleDto userResponse = UserSimpleDto.builder()
          .name(user.getName())
          .profileImageUrl(user.getProfileUrl())
          .build();

      return FeedDto.builder()
          .likes(feed.getLike())
          .hearts(feed.getHeart())
          .id(feed.getFeedId())
          .content(feed.getContent())
          .taskMessage(feed.getTask().getName())
          .content(feed.getContent())
          .profileImageUrl(feed.getImageUrl())
          .createdAt(feed.getCreatedAt())
          .user(userResponse)
          .build();
    }).toList();

    return new SearchFeedListResponse(responses);
  }

  public FetchFeedListResponse fetchFeedList() {
    List<Feed> feeds = feedRepository.findAll();

    List<FeedDto> responses = feeds.stream().map(feed -> {
      User user = feed.getUser();
      UserSimpleDto userResponse = UserSimpleDto.builder()
        .name(user.getName())
        .profileImageUrl(user.getProfileUrl())
        .build();

      return FeedDto.builder()
          .likes(feed.getLike())
          .hearts(feed.getHeart())
          .id(feed.getFeedId())
          .content(feed.getContent())
          .taskMessage(feed.getTask().getName())
          .content(feed.getContent())
          .profileImageUrl(feed.getImageUrl())
          .createdAt(feed.getCreatedAt())
          .user(userResponse)
          .build();
    }).toList();

    return new FetchFeedListResponse(responses);
  }

  public void completeTaskWithText(Long taskId, CompleteTaskWithTextRequest request){
    User currentUser = UserUtils.getUser(SecurityUtils.getAuthUserId());
    Task task = jpaTaskRepository.findById(taskId).orElseThrow(() ->
      new GeneralException(ErrorStatus.BAD_REQUEST, "해당하는 할일을 찾을 수 없습니다.")
    );

    if(task.getIsCompleted()) {
      throw new GeneralException(ErrorStatus.BAD_REQUEST, "이미 할일이 마무리되었습니다.");
    }

    if(task.getUser() != currentUser) {
      throw new GeneralException(ErrorStatus.BAD_REQUEST, "자신의 할일만 변경할 수 있습니다.");
    }

    task.updateIsCompleted(true);
    currentUser.sumTotalScore(100L);
    eventPublisher.publishEvent(new BadgeEvent(Badge.THREE_DAY));
    badgeService.handleBadgeProgress(currentUser, Badge.PERFECT_WEEK);

//    LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
//    LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
//
//    User topUser = this.userRepository.getTopRankerForToday(startOfDay, endOfDay);
//    badgeService.handleBadgeProgress(topUser, Badge.MONTHLY_KING);

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

    if(task.getIsCompleted()) {
      throw new GeneralException(ErrorStatus.BAD_REQUEST, "이미 할일이 마무리되었습니다.");
    }

    if(task.getUser() != currentUser) {
      throw new GeneralException(ErrorStatus.BAD_REQUEST, "자신의 할일만 변경할 수 있습니다.");
    }

    task.updateIsCompleted(true);
    currentUser.sumTotalScore(100L);

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
