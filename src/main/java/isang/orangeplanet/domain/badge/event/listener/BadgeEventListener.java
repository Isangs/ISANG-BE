package isang.orangeplanet.domain.badge.event.listener;

import isang.orangeplanet.domain.badge.event.BadgeEvent;
import isang.orangeplanet.domain.badge.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BadgeEventListener {
  private final BadgeService badgeService;

  @EventListener
  public void onBadgeEvent(BadgeEvent event) {
    this.badgeService.handleBadgeProgress(event.getBadge());
  }
}
