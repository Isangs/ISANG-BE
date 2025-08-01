package isang.orangeplanet.global.api_response.exception;

import isang.orangeplanet.global.api_response.ApiResponse;
import isang.orangeplanet.global.api_response.dto.ApiDto;
import isang.orangeplanet.global.api_response.status.ErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {
      String errorMessage = e.getConstraintViolations().stream()
        .map(ConstraintViolation::getMessage)
        .findFirst()
        .orElseThrow(() -> new RuntimeException("ConstraintViolationException 추출 도중 에러 발생"));

      return handleExceptionInternalConstraint(e, ErrorStatus.valueOf(errorMessage), request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException e,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request
    )  {
      Map<String, String> errors = new LinkedHashMap<>();
      e.getBindingResult().getFieldErrors()
        .forEach(fieldError -> {
          String fieldName = fieldError.getField();
          String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
          errors.merge(fieldName, errorMessage, (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", " + newErrorMessage);
        });

      return handleExceptionInternalArgs(e, ErrorStatus.BAD_REQUEST, request, errors);
    }

    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {
      e.printStackTrace();
      return handleExceptionInternalFalse(e, ErrorStatus.INTERNAL_ERROR.getHttpStatus(), request, e.getMessage());
    }

    @ExceptionHandler(value = GeneralException.class)
    public ResponseEntity<Object> onThrowException(GeneralException generalException, HttpServletRequest request) {
      ApiDto errorReasonHttpStatus = generalException.getReasonHttpStatus();
      return handleExceptionInternal(generalException, errorReasonHttpStatus, request);
    }

    private ResponseEntity<Object> handleExceptionInternal(
      Exception e,
      ApiDto reason,
      HttpServletRequest request
    ) {
      ApiResponse<Object> body = ApiResponse.onFailure(reason.getCode(), reason.getMessage(),null);
      e.printStackTrace();
      WebRequest webRequest = new ServletWebRequest(request);
      return super.handleExceptionInternal(e, body, null, reason.getHttpStatus(), webRequest);
    }

    private ResponseEntity<Object> handleExceptionInternalFalse(
      Exception e,
      HttpStatus status,
      WebRequest request,
      String errorPoint
    ) {
      ApiResponse<Object> body = ApiResponse.onFailure(ErrorStatus.INTERNAL_ERROR.getCode(), ErrorStatus.INTERNAL_ERROR.getMessage(), errorPoint);
      return super.handleExceptionInternal(e, body, HttpHeaders.EMPTY, status, request);
    }

    private ResponseEntity<Object> handleExceptionInternalArgs(
      Exception e,
      ErrorStatus errorCommonStatus,
      WebRequest request,
      Map<String, String> errorArgs
    ) {
      ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus.getCode(), errorCommonStatus.getMessage(), errorArgs);
      return super.handleExceptionInternal(e, body, HttpHeaders.EMPTY, errorCommonStatus.getHttpStatus(), request);
    }

    private ResponseEntity<Object> handleExceptionInternalConstraint(
      Exception e,
      ErrorStatus errorCommonStatus,
      WebRequest request
    ) {
      ApiResponse<Object> body = ApiResponse.onFailure(errorCommonStatus.getCode(), errorCommonStatus.getMessage(), null);
      return super.handleExceptionInternal(e, body, HttpHeaders.EMPTY, errorCommonStatus.getHttpStatus(), request);
    }
}
