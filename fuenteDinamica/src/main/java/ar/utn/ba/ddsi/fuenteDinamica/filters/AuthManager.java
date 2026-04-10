package ar.utn.ba.ddsi.fuenteDinamica.filters;

import ar.utn.ba.ddsi.fuenteDinamica.models.dtos.input.UsuarioDTOInput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/*
Recibe tokens JWT
Extrae el token y llama por HTTP a AuthService para validarlo
 */

@Component
public class AuthManager {
  private WebClient webClient;
  private final String authServiceUrl;

  public AuthManager(@Value("${auth.api.url}") String agregadorServiceUrl) {
    this.webClient = WebClient.builder().build();
    this.authServiceUrl = agregadorServiceUrl;
  }

  public boolean validarToken(String token) {
    try {
      var response = webClient.get()
          .uri(authServiceUrl + "/auth/validation")
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
          .exchangeToMono(clientResponse ->
              Mono.just(clientResponse.statusCode()))
          .block();

      return response == HttpStatus.OK;

    } catch (Exception e) {
      return false;
    }
  }

  public Long obtenerIdUsuario(String token) {
    try {
      return webClient.get()
          .uri(authServiceUrl + "/usuarios/sesion")
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
          .exchangeToMono(resp -> {
            if (resp.statusCode().is2xxSuccessful()) {
              return resp.bodyToMono(UsuarioDTOInput.class);
            }
            return Mono.empty();
          })
          .map(UsuarioDTOInput::getId)
          .block();
    } catch (Exception e) {
      return null;
    }
  }

}

