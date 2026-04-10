package ar.utn.ba.ddsi.frontend.dto;

import lombok.Data;

@Data
public class FuenteDTO {
  private String nombre;
  private TipoFuente tipoFuente;
  private String path;
  private String pathInfo;
}