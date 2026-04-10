package ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos;

import java.util.List;

import lombok.Data;
import lombok.Setter;

@Setter
@Data
public class ColeccionDTO {
  private Long id;
  private String titulo;
  private List<HechoDTO> hechos;
}
