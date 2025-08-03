package isang.orangeplanet.domain.s3_file.service;

import isang.orangeplanet.domain.s3_file.S3File;
import isang.orangeplanet.domain.s3_file.repository.S3FileRepository;
import isang.orangeplanet.global.api_response.exception.GeneralException;
import isang.orangeplanet.global.api_response.status.ErrorStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

/**
 * S3FileService : S3 파일 관련 Service
 */
@Service
@RequiredArgsConstructor
public class S3FileService {

  @Value("${spring.cloud.aws.s3.bucket}")
  private String bucket;

  private final S3Client s3Client;
  private final S3FileRepository s3FileRepository;

  /**
   * S3 파일 업로드 메서드
   * @param file : 파일
   * @return : 업로드된 S3 Url 반환
   */
  public String uploadFile(MultipartFile file) {
    String originalFileName = file.getOriginalFilename();
    String fileName = UUID.randomUUID() + "-" + originalFileName;

    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
      .bucket(bucket)
      .key(fileName)
      .contentType(file.getContentType())
      .build();

    try {
      s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
      String s3FileUrl = String.format("https://%s.s3.ap-northeast-2.amazonaws.com/%s", bucket, fileName);

      this.s3FileRepository.save(
        S3File.builder()
          .fileName(fileName)
          .originalName(originalFileName)
          .s3Url(s3FileUrl)
          .fileSize(file.getSize())
          .build()
      );

      return s3FileUrl;
    } catch (Exception e) {
      throw new GeneralException(ErrorStatus.INTERNAL_ERROR, "파일 업로드에 실패했습니다.");
    }
  }

  /**
   * S3 파일 삭제 메서드
   * @param fileUrl : S3 Url
   */
  @Transactional
  public void deleteFile(String fileUrl) {
    String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

    DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
      .bucket(bucket)
      .key(fileName)
      .build();

    s3Client.deleteObject(deleteObjectRequest);
    this.s3FileRepository.deleteByS3Url(fileUrl);
  }
}
