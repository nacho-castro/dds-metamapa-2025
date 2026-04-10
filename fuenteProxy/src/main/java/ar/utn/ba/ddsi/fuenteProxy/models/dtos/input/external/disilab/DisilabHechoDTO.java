package ar.utn.ba.ddsi.fuenteProxy.models.dtos.input.external.disilab;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DisilabHechoDTO {
  private Integer id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private Double latitud;
  private Double longitud;
  @JsonProperty("fecha_hecho")
  private LocalDateTime fechaHecho;
  @JsonProperty("created_at")
  private LocalDateTime createdAt;
}
