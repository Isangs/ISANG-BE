package isang.orangeplanet.domain.badge.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import isang.orangeplanet.domain.badge.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/badge")
@RequiredArgsConstructor
@Tag(name = "Badge", description = "뱃지 관련 API")
public class BadgeController {
  private final BadgeService badgeService;
}
