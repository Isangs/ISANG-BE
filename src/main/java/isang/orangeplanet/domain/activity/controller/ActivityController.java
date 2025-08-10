package isang.orangeplanet.domain.activity.controller;

import isang.orangeplanet.domain.activity.controller.dto.response.FetchActivityListResponse;
import isang.orangeplanet.domain.activity.service.ActivityService;
import isang.orangeplanet.global.api_response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activity")
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping
    public ApiResponse<FetchActivityListResponse> fetchActivityList(@RequestParam(required = false) Integer limit) {
        FetchActivityListResponse response = activityService.fetchActivityList(limit);
        return ApiResponse.onSuccess(response);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> removeActivityById(@PathVariable Long id) {
        activityService.removeActivityById(id);
        return ApiResponse.onSuccess();
    }
}
