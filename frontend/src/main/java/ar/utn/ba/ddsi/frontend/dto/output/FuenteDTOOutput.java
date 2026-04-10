package ar.utn.ba.ddsi.frontend.dto.output;

import lombok.Data;

@Data
public class FuenteDTOOutput {
  private Long id;
  private String nombre;
  private String tipoFuente;
  private String path;
  private String pathInfo;
  private Integer cantHechos;

}
