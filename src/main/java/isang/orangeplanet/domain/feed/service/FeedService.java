package isang.orangeplanet.domain.feed.service;

import isang.orangeplanet.domain.activity.entity.Activity;
import isang.orangeplanet.domain.activity.repository.ActivityJpaRepository;
import isang.orangeplanet.domain.auth.utils.SecurityUtils;
import isang.orangeplanet.domain.badge.event.BadgeEvent;
import isang.orangeplanet.domain.feed.Feed;
import isang.orangeplanet.domain.feed.FeedReaction;
import isang.orangeplanet.domain.feed.controller.dto.FeedDto;
import isang.orangeplanet.domain.feed.controller.dto.request.CompleteTaskWithImageRequest;
import isang.orangeplanet.domain.feed.controller.dto.response.FetchFeedListResponse;
import isang.orangeplanet.domain.feed.controller.dto.response.FetchMyFeedListResponse;
import isang.orangeplanet.domain.feed.controller.dto.response.SearchFeedListResponse;
import isang.orangeplanet.domain.feed.enums.ReactionType;
import isang.orangeplanet.domain.feed.repository.FeedReactionRepository;
import isang.orangeplanet.domain.feed.repository.FeedRepository;
import isang.orangeplanet.domain.feed.controller.dto.request.CompleteTaskWithTextRequest;
import isang.orangeplanet.domain.task.Task;
import isang.orangeplanet.domain.task.repository.JpaTaskRepository;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.domain.user.utils.UserUtils;
import isang.orangeplanet.global.api_response.exception.GeneralException;
import isang.orangeplanet.global.api_response.status.ErrorStatus;
import isang.orangeplanet.global.utils.enums.Badge;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FeedService {
  private final ApplicationEventPublisher eventPublisher;
  private final JpaTaskRepository jpaTaskRepository;
  private final FeedRepository feedRepository;
  private final ActivityJpaRepository activityJpaRepository;
  private final FeedReactionRepository feedReactionRepository;

  public void respondWithReactionType(Long id, ReactionType reactionType) {
    User user = UserUtils.getUser(SecurityUtils.getAuthUserId());
    Feed currentFeed = feedRepository.findById(id).orElseThrow(() ->
      new GeneralException(ErrorStatus.BAD_REQUEST, "해당하는 피드를 찾을 수 없습니다.")
    );

    FeedReaction feedReaction = feedReactionRepository
        .findByFeedAndUserAndReactionType(currentFeed, user, reactionType)
        .orElse(null);

    if(feedReaction == null) {
      FeedReaction newFeedReaction = FeedReaction.builder()
          .reactionType(reactionType)
          .user(user)
          .feed(currentFeed)
          .build();

      feedReactionRepository.save(newFeedReaction);

      if(reactionType == ReactionType.LIKE) {
        currentFeed.setLikes(currentFeed.getLikes() + 1);
      } else {
        currentFeed.setHearts(currentFeed.getHearts() + 1);
      }
    } else {
      feedReactionRepository.delete(feedReaction);

      if(reactionType == ReactionType.LIKE) {
        currentFeed.setLikes(currentFeed.getLikes() - 1);
      } else {
        currentFeed.setHearts(currentFeed.getHearts() - 1);
      }
    }
  }

  public SearchFeedListResponse searchFeedList(String query) {
    User user = UserUtils.getUser(SecurityUtils.getAuthUserId());
    List<Feed> feeds = feedRepository.findByUserNameLikeOrContentLikeOrTaskNameLike(query);

    return new SearchFeedListResponse(toFeedDtoList(feeds, user));
  }

  public FetchFeedListResponse fetchFeedList() {
    User user = UserUtils.getUser(SecurityUtils.getAuthUserId());
    List<Feed> feeds = feedRepository.findAll();

    return new FetchFeedListResponse(toFeedDtoList(feeds, user));
  }

  public FetchMyFeedListResponse fetchMyFeedList() {
    User user = UserUtils.getUser(SecurityUtils.getAuthUserId());
    List<Feed> feeds = feedRepository.findByTaskUser(user);

    return new FetchMyFeedListResponse(toFeedDtoList(feeds, user));
  }

  public void completeTaskWithText(Long taskId, CompleteTaskWithTextRequest request){
    Feed newFeed = completeTask(taskId, null, request.getContent());

    Activity newActivity = Activity.builder()
        .feed(newFeed)
        .build();

    activityJpaRepository.save(newActivity);

    List.of(Badge.THREE_DAY, Badge.OVER_PERFECT_SCORES, Badge.BEGINNER_ESCAPE, Badge.MASTER).forEach(badge ->
      eventPublisher.publishEvent(new BadgeEvent(badge))
    );
  }

  public void completeTaskWithImage(Long taskId, CompleteTaskWithImageRequest request){
    Feed newFeed = completeTask(taskId, request.getImageUrl(), null);

    Activity newActivity = Activity.builder()
        .feed(newFeed)
        .build();

    activityJpaRepository.save(newActivity);

    List.of(Badge.THREE_DAY, Badge.OVER_PERFECT_SCORES, Badge.BEGINNER_ESCAPE, Badge.MASTER).forEach(badge ->
      eventPublisher.publishEvent(new BadgeEvent(badge))
    );
  }

  private List<FeedDto> toFeedDtoList(List<Feed> feeds, User user) {
    Set<Long> postLikes = feedReactionRepository.findByUserAndReactionType(user, ReactionType.LIKE)
        .stream()
        .map(FeedReaction::getId)
        .collect(Collectors.toSet());

    Set<Long> postHearts = feedReactionRepository.findByUserAndReactionType(user, ReactionType.HEART)
        .stream()
        .map(FeedReaction::getId)
        .collect(Collectors.toSet());

    return feeds.stream().map(feed -> {
      Boolean isPostLiked = postLikes.contains(feed.getFeedId());
      Boolean isPostHearted = postHearts.contains(feed.getFeedId());
      return feed.toDto(isPostLiked, isPostHearted);
    }).toList();
  }

  private Feed completeTask(Long taskId, String imageUrl, String content){
    User user = UserUtils.getUser(SecurityUtils.getAuthUserId());
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
          .build();

      feedRepository.save(newFeed);
    }

    return newFeed;
  }
}
