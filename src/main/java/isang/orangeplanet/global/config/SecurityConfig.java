package isang.orangeplanet.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
          "/health", "/auth/oauth/kakao", "/auth/oauth/login/**"
        ).permitAll()
        .requestMatchers( // Swagger 관련 Url 처리
          "/v1/swagger-ui/**",
          "/v3/api-docs/**",
          "/swagger-resources/**",
          "/webjars/**"
        ).permitAll()
        .anyRequest().authenticated();
    });

//    http.addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
