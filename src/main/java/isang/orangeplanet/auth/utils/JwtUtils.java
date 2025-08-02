package isang.orangeplanet.auth.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import isang.orangeplanet.auth.dto.JwtClaimsDto;
import isang.orangeplanet.auth.dto.JwtDto;
import isang.orangeplanet.global.api_response.exception.GeneralException;
import isang.orangeplanet.global.api_response.status.ErrorStatus;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JwtUtils : Jwt 관련 서비스를 모아둔 유틸 클래스
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtils {

  @Value("${jwt.secret}")
  private String jwtSecretKey;

  private static Key secretKey;
  private static Claims claims;

  @PostConstruct
  public void init() {
    secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.jwtSecretKey));
    claims = Jwts.claims();
  }

  /**
   * Access Token과 Refresh Token 생성 메서드
   * @param dto : Jwt Claims 정보
   * @return : JwtBuilder 객체 반환
   */
  public static JwtDto createToken(JwtClaimsDto dto) {
    claims.put("name", dto.getNickName());
    claims.put("role", dto.getRole().name());

    long nowMs = System.currentTimeMillis();
    Date now = new Date(nowMs);
    Date accessExpirationDate = new Date(nowMs + (86400 * 1000));
    Date refreshExpirationDate = new Date(nowMs + (604800 * 1000));

    String accessToken = Jwts.builder()
      .setHeaderParam("typ", "JWT")
      .setHeaderParam("alg", "HS256")
      .addClaims(claims)
      .setIssuedAt(now)
      .setExpiration(accessExpirationDate)
      .signWith(secretKey)
      .compact();

    String refreshToken = Jwts.builder()
      .setHeaderParam("typ", "JWT")
      .setHeaderParam("alg", "HS256")
      .addClaims(claims)
      .setIssuedAt(now)
      .setExpiration(refreshExpirationDate)
      .signWith(secretKey)
      .compact();

    return JwtDto.builder()
      .accessToken(accessToken)
      .refreshToken(refreshToken)
      .build();
  }

  /**
   * 회원 ID 얻는 메서드
   * @param token : 토큰
   * @return : 회원 ID 반환
   */
  public static String getUserName(String token) {
    try {
      return Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .get("id", String.class);
    } catch (ExpiredJwtException e) {
      throw new GeneralException(ErrorStatus.TOKEN_EXPIRED, "만료된 토큰입니다.");
    }
  }

  /**
   * 회원 권한 얻는 메서드
   * @param token : 토큰
   * @return : 회원 권한 반환
   */
  public static String getRole(String token) {
    try {
      return Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token)
        .getBody()
        .get("role", String.class);
    } catch (ExpiredJwtException e) {
      throw new GeneralException(ErrorStatus.TOKEN_EXPIRED, "만료된 토큰입니다.");
    }
  }

  /**
   * 토큰의 만료 시간 확인 메서드
   * @param token : 토큰
   * @return : 만료 여부 반환
   */
  public static boolean getValidateToken(String token) {
    try {
      Jwts.parserBuilder()
        .setSigningKey(secretKey)
        .build()
        .parseClaimsJws(token);
      return true;
    } catch (MalformedJwtException e) {
      log.error("잘못된 JWT 토큰 > {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.error("만료된 JWT 토큰 > {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.error("지원되지 않는 JWT 토큰 > {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("JWT 문자열이 비어 있습니다 > {}", e.getMessage());
    }

    return false;
  }
}
