package ar.utn.ba.ddsi.servicioAgregador.models.dtos.input;

import lombok.Data;

@Data
public class FuenteDTOInput {
  private String nombre;
  private String tipoFuente;
  private String path;
  private String pathInfo;
}