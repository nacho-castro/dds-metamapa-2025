package ar.utn.ba.ddsi.frontend.dto.output;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudEdicionDTOOutput {

  private Long id;
  private Long idHecho;
  private String tituloHechoOriginal; // Útil para mostrar en el dashboard qué hecho se quiere editar

  // Lo que el usuario propone
  private String tituloPropuesto;
  private String descripcionPropuesta;
  private String categoriaPropuesta;

  private String motivo;
  private String estado; // PENDIENTE, ACEPTADA, RECHAZADA
  private LocalDateTime fecha;
}
