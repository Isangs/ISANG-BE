package isang.orangeplanet.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * S3Config : AWS S3 관련 설정 파일
 */
@Configuration
public class S3Config {

  @Bean
  public S3Client s3Client(AwsCredentialsProvider credentialsProvider) {
    return S3Client.builder()
      .credentialsProvider(credentialsProvider)
      .region(Region.AP_NORTHEAST_2)
      .build();
  }
}
