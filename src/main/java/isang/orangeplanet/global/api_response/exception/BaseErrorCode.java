package isang.orangeplanet.api_response.exception;

import isang.orangeplanet.api_response.dto.ApiDto;

public interface BaseErrorCode {
  ApiDto getReasonHttpStatus();
}
