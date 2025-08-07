package isang.orangeplanet.domain.badge.service;

import isang.orangeplanet.domain.badge.repository.BadgeRepository;
import isang.orangeplanet.domain.badge.repository.JpaBadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BadgeService {
  private final JpaBadgeRepository jpaBadgeRepository;
  private final BadgeRepository badgeRepository;


}
