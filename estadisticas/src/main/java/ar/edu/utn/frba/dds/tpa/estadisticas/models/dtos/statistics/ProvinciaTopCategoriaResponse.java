package ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProvinciaTopCategoriaResponse {
  private String categoria;
  private String provincia;
  private Long cantidadHechos;
}

