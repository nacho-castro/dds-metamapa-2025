package ar.utn.ba.ddsi.fuenteEstatica.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {
  @Value("${s3.bucket.name}")
  private String bucketName;

  @Value("${s3.access.key}")
  private String accessKey;

  @Value("${s3.secret.key}")
  private String secretKey;

  @Bean
  public S3Client s3Client() {
    return S3Client.builder()
        .region(Region.US_EAST_2)
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            )
        )
        .build();
  }

  //exponer el bucketName como bean
  @Bean
  public String bucketName() {
    return bucketName;
  }
}
