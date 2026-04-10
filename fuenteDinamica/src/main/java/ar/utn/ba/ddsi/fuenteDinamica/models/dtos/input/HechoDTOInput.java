package ar.utn.ba.ddsi.fuenteDinamica.models.dtos.input;

import ar.utn.ba.ddsi.fuenteDinamica.models.dtos.output.LugarDTOOutput;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HechoDTOInput {
  private String titulo;
  private String descripcion;
  private String categoria;
  private LocalDateTime fechaAcontecimiento;
  private LugarDTOOutput lugarAcontecimiento;
  private List<String> etiquetas;
  private List<String> multimediaUrls;
}
