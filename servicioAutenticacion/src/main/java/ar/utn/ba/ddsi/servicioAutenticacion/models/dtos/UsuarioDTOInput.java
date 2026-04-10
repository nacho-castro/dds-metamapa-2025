package ar.utn.ba.ddsi.servicioAutenticacion.models.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UsuarioDTOInput {
  private String nombre;
  private String password;
  private String email;
  private Integer edad;
}
