package ar.utn.ba.ddsi.frontend.dto.output;

import lombok.Data;

@Data
public class UsuarioDTOOutput {
  private Long id;
  private String nombre;
  private String rol;
  //por seguridad solo mostramos el nombre
}
