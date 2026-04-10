package ar.utn.ba.ddsi.frontend.dto.input;

import lombok.Data;

@Data
public class UsuarioDTOInput {
  private String nombre;
  private String password;
  private String email;
  private Integer edad;
}
