package isang.orangeplanet.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import isang.orangeplanet.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {
  private final AuthService authService;


}
