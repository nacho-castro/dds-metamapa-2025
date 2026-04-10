package ar.utn.ba.ddsi.fuenteEstatica.models.dtos.output;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Data
public class HechoDTOOutput {
  private String titulo;
  private String descripcion;
  private String categoria;
  private LocalDateTime fechaAcontecimiento;
  private LugarDTOOutput lugarAcontecimiento;
}
