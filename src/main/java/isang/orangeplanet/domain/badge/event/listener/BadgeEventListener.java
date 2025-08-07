package isang.orangeplanet.domain.badge.event.listener;

import isang.orangeplanet.domain.auth.utils.SecurityUtils;
import isang.orangeplanet.domain.badge.event.BadgeEvent;
import isang.orangeplanet.domain.badge.service.BadgeService;
import isang.orangeplanet.domain.user.User;
import isang.orangeplanet.domain.user.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BadgeEventListener {
  private final BadgeService badgeService;

  @EventListener
  public void onBadgeEvent(BadgeEvent event) {
    User user = UserUtils.getUser(SecurityUtils.getAuthUserId());
    this.badgeService.handleBadgeProgress(user, event.getBadge());
  }
}
