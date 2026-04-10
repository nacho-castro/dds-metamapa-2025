package ar.utn.ba.ddsi.frontend.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudAdminDTO {
  private Long id;
  private Long idHecho; // ID del hecho que se solicita eliminar
  private String motivoBorrado;
  private String estado;
  private LocalDateTime fecha;
}
