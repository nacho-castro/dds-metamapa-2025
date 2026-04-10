package ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.externalDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColeccionDTOInput {
  private Long id;
  private String titulo;
  private String descripcion;
  private TiposAlgoritmos algoritmoConsenso;
}
