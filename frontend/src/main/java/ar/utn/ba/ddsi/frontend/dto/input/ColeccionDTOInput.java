package ar.utn.ba.ddsi.frontend.dto.input;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ColeccionDTOInput {
  private String titulo;
  private String descripcion;
  private String algoritmoConsenso;
  private List<Long> fuentes;
  private List<Long> criterios;
}
