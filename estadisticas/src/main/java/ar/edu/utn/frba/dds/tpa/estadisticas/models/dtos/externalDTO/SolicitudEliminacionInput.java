package ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.externalDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SolicitudEliminacionInput {
  private Long id;
  private Long idHecho; // ID del hecho que se solicita eliminar
  private String motivoBorrado;
  private String estado;
  private LocalDateTime fecha;
}