package ar.utn.ba.ddsi.servicioAgregador.models.dtos.output;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.algoritmosConsenso.TiposAlgoritmos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
public class ColeccionDTOOutput {
  private Long id;
  private String titulo;
  private String descripcion;
  private TiposAlgoritmos algoritmoConsenso;
}
