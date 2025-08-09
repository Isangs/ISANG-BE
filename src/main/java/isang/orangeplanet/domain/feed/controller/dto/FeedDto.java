package isang.orangeplanet.domain.feed.controller.dto;

import isang.orangeplanet.domain.user.controller.dto.UserSimpleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedDto {
    private Long id;
    private String taskMessage;
    private String content;
    private String profileImageUrl;
    private Long hearts;
    private Boolean isPublic;
    private Boolean isPostLiked;
    private Boolean isPostHearted;
    private Long likes;
    private LocalDateTime createdAt;
    private UserSimpleDto user;
}
