package ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.externalDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HechoDTOInput {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private LocalDateTime fechaAcontecimiento;
  private LocalDate fechaCarga;
  private LugarDTOInput lugarAcontecimiento;
  private Boolean activo;
  private Long usuarioId;
  private List<Long> fuentes;
}