package ar.utn.ba.ddsi.frontend.services;

import ar.utn.ba.ddsi.frontend.dto.input.UsuarioDTOInput;
import ar.utn.ba.ddsi.frontend.dto.output.UsuarioDTOOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class UsuarioService {
  private WebClient webClient;
  private final String authServiceUrl;

  @Autowired
  public UsuarioService(@Value("${auth.api.url}") String authServiceUrl) {
    this.webClient = WebClient.builder().build();
    this.authServiceUrl = authServiceUrl;
  }

  //CrearUsuario no requiere Token
  public UsuarioDTOOutput crearCuenta(UsuarioDTOInput usuarioDTO) {
    try {
      //HTTP al servicio externo de Autenticacion
      UsuarioDTOOutput response = webClient
          .post()
          .uri(authServiceUrl + "/usuarios")
          .bodyValue(usuarioDTO)
          .retrieve()
          .bodyToMono(UsuarioDTOOutput.class)
          .block();
      return response;
    } catch (WebClientResponseException e) {
      // Otros errores HTTP
      throw new RuntimeException("Error en el servicio de autenticación: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RuntimeException("Error de conexión con el servicio de autenticación: " + e.getMessage(), e);
    }
  }
}
