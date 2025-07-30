package isang.orangeplanet.api_response.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
public class ApiDto {
  private HttpStatus httpStatus;
  private final Boolean isSuccess;
  private final String code;
  private final String  message;
}
