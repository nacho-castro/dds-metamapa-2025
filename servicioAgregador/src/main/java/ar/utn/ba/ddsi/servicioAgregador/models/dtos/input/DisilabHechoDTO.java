package ar.utn.ba.ddsi.servicioAgregador.models.dtos.input;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class DisilabHechoDTO {
  private Integer id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private Double latitud;
  private Double longitud;
  private LocalDateTime fechaHecho;
  private LocalDateTime createdAt;
}
