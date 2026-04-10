package ar.utn.ba.ddsi.servicioAgregador.models.dtos.input;

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
  private String nuevoTitulo;
  private String nuevaDescripcion;
  private String nuevaCategoria;

  private String motivo;
}
