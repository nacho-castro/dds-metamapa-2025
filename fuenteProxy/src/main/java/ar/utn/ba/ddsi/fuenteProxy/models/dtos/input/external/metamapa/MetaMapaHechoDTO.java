package ar.utn.ba.ddsi.fuenteProxy.models.dtos.input.external.metamapa;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MetaMapaHechoDTO {
  private Integer id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private Double latitud;
  private Double longitud;
  private LocalDateTime fechaHecho;
  private LocalDateTime createdAt;
}
