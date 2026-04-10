package ar.utn.ba.ddsi.frontend.dto.input;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class HechoDTOInput {
  private String titulo;
  private String descripcion;
  private String categoria;
  private LugarDTOInput lugarAcontecimiento;
  private LocalDateTime fechaAcontecimiento;
  private List<String> multimediaUrls;
}
