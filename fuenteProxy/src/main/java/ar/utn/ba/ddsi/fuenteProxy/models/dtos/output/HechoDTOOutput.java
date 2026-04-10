package ar.utn.ba.ddsi.fuenteProxy.models.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HechoDTOOutput {
  private String titulo;
  private String descripcion;
  private String categoria;
  private LocalDateTime fechaAcontecimiento;
  private LugarDTOOutput lugarAcontecimiento;
  private List<EtiquetaDTOOutput> etiquetas;

  public HechoDTOOutput(String titulo, String descripcion, String categoria, LocalDateTime fechaAcontecimiento, LugarDTOOutput lugarAcontecimiento) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.categoria = categoria;
    this.fechaAcontecimiento = fechaAcontecimiento;
    this.lugarAcontecimiento = lugarAcontecimiento;
  }
}
