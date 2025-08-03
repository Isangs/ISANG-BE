package isang.orangeplanet.domain.goal.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import isang.orangeplanet.domain.goal.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/goal")
@RequiredArgsConstructor
@Tag(name = "Goal", description = "목표 관련 API")
public class GoalController {
  private final GoalService goalService;


}
