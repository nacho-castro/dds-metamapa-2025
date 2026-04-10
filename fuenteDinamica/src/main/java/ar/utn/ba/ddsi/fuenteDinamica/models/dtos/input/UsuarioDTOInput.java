package ar.utn.ba.ddsi.fuenteDinamica.models.dtos.input;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UsuarioDTOInput {
  private Long id;
  private String nombre;
  private String rol;
}
