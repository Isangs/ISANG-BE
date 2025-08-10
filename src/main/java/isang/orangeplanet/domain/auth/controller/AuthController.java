package isang.orangeplanet.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import isang.orangeplanet.domain.auth.controller.response.GetAuthInfoResponse;
import isang.orangeplanet.domain.auth.controller.response.GetTokenResponse;
import isang.orangeplanet.domain.auth.service.AuthService;
import isang.orangeplanet.global.api_response.ApiResponse;
import isang.orangeplanet.global.api_response.exception.GeneralException;
import isang.orangeplanet.global.api_response.status.ErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * AuthController : 인증 관련 Controller
 */
@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {
  private final AuthService authService;

  /**
   * 카카오 로그인 화면 리다이렉션 엔드포인트
   * @param response : HttpServletResponse 객체
   * @return : 공통 응답 포맷 반환 및 지정한 Url로 리다이렉션
   */
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

  /**
   * 로그인 엔드포인트
   * @param code : 인가 코드
   * @return : GetAuthInfoResponse 객체 반환
   */
  @PostMapping(value = "/oauth/login/{code}")
  @Operation(summary = "로그인", description = "로그인 엔드포인트")
  public ApiResponse<GetAuthInfoResponse> kakaoLogin(@PathVariable("code") String code) {
    return ApiResponse.onSuccess(authService.kakaoOAuth2Login(code));
  }

  /**
   * 로그인 엔드포인트
   * @param code : 인가 코드
   * @return : GetAuthInfoResponse 객체 반환
   */
  @PostMapping(value = "/oauth/login/{code}/app")
  @Operation(summary = "로그인", description = "로그인 엔드포인트")
  public ApiResponse<GetAuthInfoResponse> appKakaoLogin(@PathVariable("code") String code) {
    return ApiResponse.onSuccess(authService.appKakaoOAuth2Login(code));
  }

  /**
   * Access Token 재발급 엔드포인트
   * @param request : HttpServletRequest 객체
   * @return : 재발급된 Access Token 반환
   */
  @PostMapping(value = "/refresh")
  @Operation(summary = "Access Token 재발급", description = "새로운 Access Token 발급 엔드포인트")
  public ApiResponse<GetTokenResponse> recreateAccessToken(@NonNull HttpServletRequest request) {
    return ApiResponse.onSuccess(authService.recreateAccessToken(request));
  }

  /**
   * 로그아웃 엔드포인트
   * @return : 공통 응답 포맷 반환
   */
  @DeleteMapping(value = "/logout")
  @Operation(summary = "로그아웃", description = "로그아웃 엔드포인트")
  public ApiResponse<Void> logout() {
    authService.logout();
    return ApiResponse.onSuccess();
  }
}
