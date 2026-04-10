package ar.utn.ba.ddsi.frontend.dto.input;

import lombok.Data;

@Data
public class EditFuenteDTO {
  private String nombre;
  private String tipoFuente;
  private String path;
  private String pathInfo;
}
