package isang.orangeplanet.domain.activity.controller;

import isang.orangeplanet.domain.activity.controller.dto.response.FetchActivityListResponse;
import isang.orangeplanet.domain.activity.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activity")
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping
    public ResponseEntity<FetchActivityListResponse> fetchActivityList(@RequestParam(required = false) Integer limit) {
        FetchActivityListResponse response = activityService.fetchActivityList(limit);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeActivityById(@PathVariable Long id) {
        activityService.removeActivityById(id);
        return ResponseEntity.ok().build();
    }
}
