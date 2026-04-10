package ar.utn.ba.ddsi.fuenteProxy.models.dtos.input.external.api;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiHechoDTO {
  private Integer id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private Double latitud;
  private Double longitud;
  private LocalDateTime fechaHecho;
  private LocalDateTime createdAt;
}
