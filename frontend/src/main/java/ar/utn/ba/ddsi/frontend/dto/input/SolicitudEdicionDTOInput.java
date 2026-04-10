package ar.utn.ba.ddsi.frontend.dto.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudEdicionDTOInput {

  private Long idHecho;

  // Datos propuestos
  private String nuevoTitulo;
  private String nuevaDescripcion;
  private String nuevaCategoria;

  private String motivo;
}
