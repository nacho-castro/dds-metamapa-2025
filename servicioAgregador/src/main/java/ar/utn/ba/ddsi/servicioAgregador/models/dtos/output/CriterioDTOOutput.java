package ar.utn.ba.ddsi.servicioAgregador.models.dtos.output;

import lombok.Data;

@Data
public class CriterioDTOOutput {
  private Long id;
  private String tipo;
  private Object parametros;
}
