package isang.orangeplanet.global.api_response.exception;

import isang.orangeplanet.global.api_response.dto.ApiDto;
import isang.orangeplanet.global.api_response.status.ErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

  private final BaseErrorCode code;

  public GeneralException(ErrorStatus errorStatus, String message) {
    super(errorStatus.getMessage(message));
    this.code = errorStatus;
  }

  public ApiDto getReasonHttpStatus() {
    return this.code.getReasonHttpStatus();
  }
}
