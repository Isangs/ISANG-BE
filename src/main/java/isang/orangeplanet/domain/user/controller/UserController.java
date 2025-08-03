package isang.orangeplanet.domain.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import isang.orangeplanet.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "회원 관련 API")
public class UserController {
  private final UserService userService;


}
