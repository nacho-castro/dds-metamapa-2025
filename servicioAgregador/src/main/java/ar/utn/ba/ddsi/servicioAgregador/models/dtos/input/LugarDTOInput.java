package ar.utn.ba.ddsi.servicioAgregador.models.dtos.input;

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
public class LugarDTOInput {
  private double latitud;
  private double longitud;
}
