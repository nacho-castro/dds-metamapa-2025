package ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HoraTopCategoriaResponse {
  private String categoria;
  private Integer hora; // 0 a 23
  private Long cantidadHechos;
}
