package isang.orangeplanet.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SwaggerConfig : Swagger 관련 설정 파일
 */
@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    Info info = new Info()
      .title("Isang API 명세서")
      .description("Isang API")
      .version("v1");

    // JWT 구현 시 제거
//    String jwtSchemeName = "JWT TOKEN";
//    SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
//
//    Components components = new Components()
//      .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
//        .name("Authorization")
//        .type(SecurityScheme.Type.HTTP)
//        .scheme("bearer")
//        .bearerFormat("JWT")
//        .in(SecurityScheme.In.HEADER));

    return new OpenAPI()
      .info(info)
      .addServersItem(new Server().url("/"));
//      .addSecurityItem(securityRequirement)
//      .components(components);
  }
}
