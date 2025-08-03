package isang.orangeplanet.domain.s3_file.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import isang.orangeplanet.domain.s3_file.service.S3FileService;
import isang.orangeplanet.global.api_response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * S3FileController : S3 파일 관련 Controller
 */
@RestController
@RequestMapping(value = "/s3")
@RequiredArgsConstructor
@Tag(name = "S3", description = "S3 관련 API")
public class S3FileController {
  private final S3FileService s3Service;

  /**
   * S3 파일 업로드 엔드포인트
   * @param file : 파일
   * @return : S3 파일 Url 반환
   */
  @PostMapping(value = "/upload", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
  @Operation(summary = "S3 파일 업로드", description = "S3 파일 업로드 엔드포인트")
  public ApiResponse<String> uploadFile(@RequestPart MultipartFile file) {
    return ApiResponse.onSuccess(s3Service.uploadFile(file));
  }

  /**
   * S3 파일 삭제 엔드포인트
   * @param s3Url : S3 Url
   * @return : 공통 응답 객체 반환
   */
  @DeleteMapping(value = "/delete")
  @Operation(summary = "S3 파일 삭제", description = "S3 파일 삭제 엔드포인트")
  public ApiResponse<Void> deleteFile(@RequestParam("s3Url") String s3Url) {
    s3Service.deleteFile(s3Url);
    return ApiResponse.onSuccess();
  }
}
