package ar.utn.ba.ddsi.frontend.dto.output;

import ar.utn.ba.ddsi.frontend.dto.LugarDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HechoDTOOutput {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private LocalDateTime fechaAcontecimiento;
  private LocalDate fechaCarga;
  private LugarDTO lugarAcontecimiento;
  private Boolean activo;
  private List<String> etiquetas;
  private List<String> multimediaUrls;
}
