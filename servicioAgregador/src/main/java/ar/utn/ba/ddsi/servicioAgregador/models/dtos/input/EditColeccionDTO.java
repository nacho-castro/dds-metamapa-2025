package ar.utn.ba.ddsi.servicioAgregador.models.dtos.input;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.algoritmosConsenso.TiposAlgoritmos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Data
public class EditColeccionDTO {
  private String titulo;
  private String descripcion;
}
