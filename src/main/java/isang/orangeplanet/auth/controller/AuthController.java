package isang.orangeplanet.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import isang.orangeplanet.auth.controller.response.GetAuthInfoResponse;
import isang.orangeplanet.auth.controller.response.GetTokenResponse;
import isang.orangeplanet.auth.service.AuthService;
import isang.orangeplanet.global.api_response.ApiResponse;
import isang.orangeplanet.global.api_response.exception.GeneralException;
import isang.orangeplanet.global.api_response.status.ErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {
  private final AuthService authService;

  @GetMapping(value = "/oauth/kakao")
  @Operation(summary = "카카오 로그인 화면 호출", description = "카카오 로그인 화면 호출 엔드포인트")
  public ApiResponse<Void> kakaoLogin(@NonNull HttpServletResponse response) {
    try {
      response.sendRedirect(authService.kakaoLoginUrl());
      return ApiResponse.onSuccess();
    } catch (IOException e) {
      throw new GeneralException(ErrorStatus.INTERNAL_ERROR, "카카오 로그인 화면 요청중 문제가 발생했습니다.");
    }
  }

  @PostMapping(value = "/oauth/login/{code}")
  @Operation(summary = "로그인", description = "로그인 엔드포인트")
  public ApiResponse<GetAuthInfoResponse> kakaoLogin(@PathVariable("code") String code) {
    return ApiResponse.onSuccess(authService.kakaoOAuth2Login(code));
  }

  @PostMapping(value = "/refresh")
  @Operation(summary = "Access Token 재발급", description = "새로운 Access Token 발급 엔드포인트")
  public ApiResponse<GetTokenResponse> recreateAccessToken(@NonNull HttpServletRequest request) {
    return ApiResponse.onSuccess(authService.recreateAccessToken(request));
  }
}
