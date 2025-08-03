package isang.orangeplanet.global.config;

import isang.orangeplanet.domain.auth.filter.AuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public AuthFilter authFilter() {
    return new AuthFilter();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.httpBasic(AbstractHttpConfigurer::disable)
      .formLogin(AbstractHttpConfigurer::disable)
      .csrf(AbstractHttpConfigurer::disable)
      .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      );

    http.authorizeHttpRequests(authorizeRequests -> {
      authorizeRequests.requestMatchers( // Security 인증 filter 패스
        "/health", "/auth/oauth/kakao", "/auth/oauth/login/**", "/s3/**"
      ).permitAll()
      .requestMatchers( // Swagger 관련 Url 처리
        "/v3/api-docs/**",
        "/swagger-ui.html",
        "/swagger-ui/**",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/webjars/**"
      ).permitAll()
      .anyRequest().authenticated();
    });

    http.addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
