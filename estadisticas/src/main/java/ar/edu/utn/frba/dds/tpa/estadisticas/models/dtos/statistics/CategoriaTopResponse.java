package ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoriaTopResponse {
  private Long coleccionId;
  private String categoria;
  private Long cantidadHechos;
}
