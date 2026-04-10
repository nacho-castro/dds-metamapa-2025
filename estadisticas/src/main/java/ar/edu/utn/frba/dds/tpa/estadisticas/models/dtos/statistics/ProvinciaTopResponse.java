package ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProvinciaTopResponse {
  private Long coleccionId;
  private String provincia;
  private Long cantidadHechos;
}
