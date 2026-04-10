package ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.externalDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LugarDTOInput {
  private double latitud;
  private double longitud;
  private String provincia;
}
