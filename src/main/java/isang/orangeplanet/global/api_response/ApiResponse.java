package isang.orangeplanet.global.api_response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import isang.orangeplanet.global.api_response.status.SuccessStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@JsonPropertyOrder
public class ApiResponse<T> {

  private final Boolean isSuccess;
  private final String code;
  private final String message;

  @JsonInclude(Include.NON_NULL)
  private T result;

  public static <T> ApiResponse<T> onSuccess(T result) {
    return new ApiResponse<>(true, SuccessStatus.OK.getCode(), SuccessStatus.OK.getMessage(), result);
  }

  public static <T> ApiResponse<T> onSuccess() {
    return new ApiResponse<>(true, SuccessStatus.OK.getCode(), SuccessStatus.OK.getMessage(), null);
  }

  public static <T> ApiResponse<T> onFailure(String code, String message, T data) {
    return new ApiResponse<>(false, code, message, data);
  }
}
