package ar.utn.ba.ddsi.frontend.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColeccionDTOOutput {
  private Long id;
  private String titulo;
  private String descripcion;
  private String algoritmoConsenso;
  private List<Long> fuentes;
}
