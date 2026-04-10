package ar.utn.ba.ddsi.fuenteProxy.models.dtos.input.external.disilab;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class LoginDTO {
  private String email;
  private String password;

  public LoginDTO(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
