package isang.orangeplanet.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import isang.orangeplanet.domain.user.controller.request.UpdateUserRequest;
import isang.orangeplanet.domain.user.controller.response.DetailUserResponse;
import isang.orangeplanet.domain.user.service.UserService;
import isang.orangeplanet.global.api_response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "회원 관련 API")
public class UserController {
  private final UserService userService;

  @GetMapping(value = "/detail")
  @Operation(summary = "회원 상세 정보 조회", description = "회원 상세 정보 조회 엔드포인트")
  public ApiResponse<DetailUserResponse> getDetailUser() {
    return ApiResponse.onSuccess(this.userService.getDetailUser());
  }

  @PatchMapping(value = "/update")
  @Operation(summary = "회원 정보 수정", description = "회원 정보 수정 엔드포인트")
  public ApiResponse<Void> updateUser(@RequestBody UpdateUserRequest request) {
    this.userService.updateUser(request);
    return ApiResponse.onSuccess();
  }
}
