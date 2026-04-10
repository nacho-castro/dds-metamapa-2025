package ar.utn.ba.ddsi.fuenteEstatica.config;

import ar.utn.ba.ddsi.fuenteEstatica.models.entities.datasets.Dataset;
import ar.utn.ba.ddsi.fuenteEstatica.models.entities.datasets.DatasetCsv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class DatasetConfig {
  private final S3Client s3Client;
  private final String bucketName;

  public DatasetConfig(S3Client s3Client, @Value("${s3.bucket.name}") String bucketName) {
    this.s3Client = s3Client;
    this.bucketName = bucketName;
  }

  @Bean
  public Dataset datasetCsv() {
    return new DatasetCsv("archivo.csv", s3Client, bucketName);
  }
}