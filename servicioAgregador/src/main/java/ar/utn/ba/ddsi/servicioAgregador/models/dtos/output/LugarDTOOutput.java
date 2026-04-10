package ar.utn.ba.ddsi.servicioAgregador.models.dtos.output;

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
  private String provincia;
}
