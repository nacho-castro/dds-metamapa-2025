package ar.utn.ba.ddsi.fuenteDinamica.models.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LugarDTOOutput {
  private double latitud;
  private double longitud;
}
