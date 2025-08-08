package isang.orangeplanet.domain.activity.service;

import isang.orangeplanet.domain.activity.controller.dto.response.FetchActivityListResponse;
import isang.orangeplanet.domain.activity.controller.dto.response.FetchActivityResponse;
import isang.orangeplanet.domain.activity.entity.Activity;
import isang.orangeplanet.domain.activity.repository.ActivityJpaRepository;
import isang.orangeplanet.domain.auth.utils.SecurityUtils;
import isang.orangeplanet.domain.feed.Feed;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.domain.user.utils.UserUtils;
import isang.orangeplanet.global.api_response.exception.GeneralException;
import isang.orangeplanet.global.api_response.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityJpaRepository activityJpaRepository;

    public FetchActivityListResponse fetchActivityList(Integer limit) {
        User user = UserUtils.getUser(SecurityUtils.getAuthUserId());

        List<Activity> activityList;
        if(limit == null) {
            activityList = activityJpaRepository.findByFeedUser(user);
        } else {
            PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
            activityList = activityJpaRepository.findByFeedUser(user, pageRequest).stream().toList();
        }

        List<FetchActivityResponse> responses = activityList.stream().map(activity -> FetchActivityResponse.builder()
            .content(activity.getFeed().getContent())
            .taskMessage(activity.getFeed().getTask().getName())
            .createdAt(activity.getFeed().getCreatedAt())
            .build()
        ).toList();

        return new FetchActivityListResponse(responses);
    }

    public void removeActivityById(Long id) {
        User user = UserUtils.getUser(SecurityUtils.getAuthUserId());
        Activity activity = activityJpaRepository.findById(id).orElseThrow(() ->
            new GeneralException(ErrorStatus.BAD_REQUEST, "해당하는 활동을 찾을 수 없습니다.")
        );
        Feed feed = activity.getFeed();

        if(feed.getUser() != user) {
            throw new GeneralException(ErrorStatus.BAD_REQUEST, "자신의 활동만 삭제할 수 있습니다.");
        }

        activityJpaRepository.delete(activity);
    }
}
