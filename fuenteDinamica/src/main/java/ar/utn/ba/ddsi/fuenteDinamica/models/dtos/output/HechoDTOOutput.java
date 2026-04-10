package ar.utn.ba.ddsi.fuenteDinamica.models.dtos.output;

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
@AllArgsConstructor
@NoArgsConstructor
public class HechoDTOOutput {
  private String titulo;
  private String descripcion;
  private String categoria;
  private LugarDTOOutput lugarAcontecimiento;
  private LocalDateTime fechaAcontecimiento;
  private LocalDateTime fechaCarga;
  private List<String> etiquetas;
  private List<String> multimediaUrls;
  private Long creador;
  private Boolean editable;
}
