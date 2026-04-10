package ar.utn.ba.ddsi.servicioAgregador.models.dtos.input;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Etiqueta;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Lugar;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
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
  private Long fuenteid;
  private Long usuarioid;
}
