package ar.utn.ba.ddsi.servicioAgregador.models.dtos.input;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HechoDinamicoDTOInput {
  private String titulo;
  private String descripcion;
  private String categoria;
  private LocalDateTime fechaAcontecimiento;
  private LugarDTOInput lugarAcontecimiento;
  private List<String> etiquetas;
}
