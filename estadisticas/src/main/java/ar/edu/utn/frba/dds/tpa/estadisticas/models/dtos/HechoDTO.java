package ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HechoDTO {
  private Long id;
  private String categoria;
  private String provincia;
  private LocalDateTime fechaAcontecimiento;
}
