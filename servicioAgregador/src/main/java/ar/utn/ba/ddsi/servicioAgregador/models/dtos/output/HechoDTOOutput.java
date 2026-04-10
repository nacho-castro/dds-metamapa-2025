package ar.utn.ba.ddsi.servicioAgregador.models.dtos.output;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.TipoFuente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
public class HechoDTOOutput {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private LocalDateTime fechaAcontecimiento;
  private LocalDate fechaCarga;
  private LugarDTOOutput lugarAcontecimiento;
  private Boolean activo;
  private Long usuarioId;
  private List<Long> fuentes;
}
