package isang.orangeplanet.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import isang.orangeplanet.auth.utils.JwtUtils;
import isang.orangeplanet.global.api_response.ApiResponse;
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

public class AuthFilter extends OncePerRequestFilter {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final List<String> EXCLUDE_URLS = List.of(
    "/health",
    "/auth/oauth/kakao",
    "/auth/oauth/login"
  );

  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    for (String uri : EXCLUDE_URLS) {
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
      } else {
        String authRefreshTokenHeader = request.getHeader("Refresh-Token");

        if (authRefreshTokenHeader != null && authHeader.startsWith("Bearer ")) {
          this.setAuthentication(id, role);
        } else {
          filterChain.doFilter(request, this.errorResponse(response));
          return;
        }
      }
    }

    filterChain.doFilter(request, response);
  }

  private void setAuthentication(String id, String role) {
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
      id, null, List.of(new SimpleGrantedAuthority(role))
    );
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

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
