package ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos;

import ar.edu.utn.frba.dds.tpa.estadisticas.models.entities.Estado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudDTO {
  private Long id;
  private Estado estado;

  public SolicitudDTO(Long id) {
    this.id = id;
  }
}
