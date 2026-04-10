package ar.utn.ba.ddsi.fuenteEstatica.models.entities.datasets;

import ar.utn.ba.ddsi.fuenteEstatica.models.entities.hechos.Hecho;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
public class DatasetCsv implements Dataset {
  private String filename;
  private S3Client s3Client;
  private String bucketName;

  public DatasetCsv(String filename, S3Client s3Client, String bucketName) {
    this.filename = filename;
    this.s3Client = s3Client;
    this.bucketName = bucketName;
  }

  @Override
  public List<Hecho> obtenerInformacion() {
    List<Hecho> listadoHechosObtenidos = new ArrayList<>();

    try {
      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
          .bucket(bucketName)
          .key("csv/" + filename) // opcional: podés organizar carpetas
          .build();

      InputStream inputStream = s3Client.getObject(getObjectRequest);
      BufferedReader bufferlector = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

      bufferlector.readLine(); // Saltear encabezado
      String lineaArchivo;
      while ((lineaArchivo = bufferlector.readLine()) != null) {
        Hecho hecho = ImportadorCSV.parsearLinea(lineaArchivo);
        if (hecho != null) listadoHechosObtenidos.add(hecho);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return listadoHechosObtenidos;
  }
}
