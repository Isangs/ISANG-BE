package isang.orangeplanet.global.api_response.exception;

import isang.orangeplanet.global.api_response.dto.ApiDto;

public interface BaseErrorCode {
  ApiDto getReasonHttpStatus();
}
