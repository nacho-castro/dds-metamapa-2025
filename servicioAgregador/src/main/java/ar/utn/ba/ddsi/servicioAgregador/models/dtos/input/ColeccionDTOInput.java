package ar.utn.ba.ddsi.servicioAgregador.models.dtos.input;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.algoritmosConsenso.TiposAlgoritmos;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.FuenteAlt;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Data
public class ColeccionDTOInput {
  private String titulo;
  private String descripcion;
  private TiposAlgoritmos algoritmoConsenso;
  private List<Long> fuentes; //IDs fuentes ya existentes
  private List<Long> criterios; //IDs criterios ya existentes
}
