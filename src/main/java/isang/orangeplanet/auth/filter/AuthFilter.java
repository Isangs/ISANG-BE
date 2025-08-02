package isang.orangeplanet.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import isang.orangeplanet.auth.utils.JwtUtils;
import isang.orangeplanet.global.api_response.ApiResponse;
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

  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String accessToken = authHeader.substring(7);
      String name = JwtUtils.getUserName(accessToken);
      String role = JwtUtils.getRole(accessToken);

      if (JwtUtils.getValidateToken(accessToken)) {
        this.setAuthentication(name, role);
      } else {
        if (request.getHeader("Refresh-Token") == null) {
          String errorResponse = objectMapper.writeValueAsString(
            ApiResponse.onFailure("TOKEN_EXPIRED", "만료된 토큰입니다.", null)
          );

          response.setContentType("application/json;charset=UTF-8");
          response.getWriter().write(errorResponse);
        }
      }
    }

    filterChain.doFilter(request, response);
  }

  private void setAuthentication(String name, String role) {
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
      name, null,
      List.of(new SimpleGrantedAuthority(role))
    );
    SecurityContextHolder.getContext().setAuthentication(auth);
  }
}
