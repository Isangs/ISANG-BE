package isang.orangeplanet.api_response.status;

import isang.orangeplanet.api_response.dto.ApiDto;
import isang.orangeplanet.api_response.exception.BaseErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

  INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON5000"),

  BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON4001"),
  NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON4002"),
  KEY_NOT_EXIST(HttpStatus.NOT_FOUND, "COMMON4003"),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401"),
  TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN_EXPIRED");

  private final HttpStatus httpStatus;
  private final String code;
  private String message;

  public String getMessage(String message) {
    this.message = message;
    return Optional.ofNullable(message)
      .filter(Predicate.not(String::isBlank))
      .orElse(this.getMessage());
  }

  @Override
  public ApiDto getReasonHttpStatus() {
    return ApiDto.builder()
      .message(message)
      .code(code)
      .isSuccess(false)
      .httpStatus(httpStatus)
      .build();
  }
}
