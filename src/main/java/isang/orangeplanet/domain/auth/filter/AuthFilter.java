package isang.orangeplanet.domain.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import isang.orangeplanet.domain.auth.utils.JwtUtils;
import isang.orangeplanet.global.api_response.exception.GeneralException;
import isang.orangeplanet.global.api_response.status.ErrorStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * AuthFilter : Jwt 인증 필터
 */
public class AuthFilter extends OncePerRequestFilter {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final List<String> EXCLUDE_URLS = List.of(
    "/health",
    "/auth/oauth/kakao",
    "/auth/oauth/login"
  );

  /**
   * Jwt 인증 필터 메서드
   * @param request : HttpServletRequest 객체
   * @param response : HttpServletResponse 객체
   * @param filterChain : FilterChain 객체
   * @throws ServletException : 예외
   * @throws IOException : 예외
   */
  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    for (String uri : EXCLUDE_URLS) { // 인증이 필요하지 않은 자원은 패스
      if (request.getRequestURI().contains(uri)) {
        filterChain.doFilter(request, response);
        return;
      }
    }

    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {

      String accessToken = authHeader.substring(7);
      String id = JwtUtils.getUserId(accessToken);
      String role = JwtUtils.getRole(accessToken);

      if (JwtUtils.getValidateToken(accessToken)) {
        this.setAuthentication(id, role);
      } else { // 토큰의 만료 기간이 지났을 경우
        if (request.getRequestURI().contains("/auth/refresh")) { // Access Token을 재발급 받을려는 경우
          String authRefreshTokenHeader = request.getHeader("Refresh-Token"); // 반드시 필요함!!
          if (authRefreshTokenHeader != null && authHeader.startsWith("Bearer ")) this.setAuthentication(id, role);
        } else {
          filterChain.doFilter(request, this.errorResponse(response));
          return;
        }
      }
    }

    filterChain.doFilter(request, response);
  }

  /**
   * SecurityContextHolder에 인증 정보 저장
   * @param id : 회원 ID
   * @param role : 권한
   */
  private void setAuthentication(String id, String role) {
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
      id, null, List.of(new SimpleGrantedAuthority(role))
    );
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  /**
   * 에러 메시지 제작 메서드
   * @param response : HttpServletResponse 객체
   * @return : HttpServletResponse 객체 반환
   */
  private HttpServletResponse errorResponse(HttpServletResponse response) {
    try {
      String errorResponse = objectMapper.writeValueAsString(
        new GeneralException(ErrorStatus.TOKEN_EXPIRED, "만료된 토큰입니다.")
      );

      response.setContentType("application/json;charset=UTF-8");
      response.getWriter().write(errorResponse);

      return response;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
