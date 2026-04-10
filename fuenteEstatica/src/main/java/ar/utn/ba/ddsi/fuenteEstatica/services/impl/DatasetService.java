package ar.utn.ba.ddsi.fuenteEstatica.services.impl;

import ar.utn.ba.ddsi.fuenteEstatica.models.entities.datasets.Dataset;
import ar.utn.ba.ddsi.fuenteEstatica.models.entities.datasets.DatasetCsv;
import ar.utn.ba.ddsi.fuenteEstatica.models.entities.datasets.DatasetType;
import ar.utn.ba.ddsi.fuenteEstatica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteEstatica.services.IDatasetService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.List;

@Service
public class DatasetService implements IDatasetService {

  private final S3Client s3Client;
  private final String bucketName;

  public DatasetService(S3Client s3Client, @Value("${s3.bucket.name}") String bucketName) {
    this.s3Client = s3Client;
    this.bucketName = bucketName;
  }

  @Override
  public List<Hecho> obtenerInformacion(Dataset dataset) {
    List<Hecho> hechosObtenidos = dataset.obtenerInformacion();
    return hechosObtenidos;
  }

  //PATRON FACTORY METHOD:
  //Recibe un parámetro (el tipo de dataset)
  //Crea una subclase concreta (DatasetCsv, DatasetDb, etc.)
  //Oculta la lógica de construcción al cliente
  @Override
  public Dataset construirDataset(DatasetType tipo, String path) {
    return switch (tipo) {
      case CSV -> new DatasetCsv(path, s3Client, bucketName);
      case DB -> null;
    };
  }
}
