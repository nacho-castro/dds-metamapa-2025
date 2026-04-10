package ar.utn.ba.ddsi.servicioAgregador.filters;

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
      var status = webClient
          .get()
          .uri(authServiceUrl + "/auth/validation")
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
          .retrieve()
          .toBodilessEntity()     // no esperamos body, solo el status
          .map(response -> response.getStatusCode())
          .onErrorResume(e -> Mono.just(HttpStatus.UNAUTHORIZED))
          .block();

      System.out.println("AuthService respondió: " + status);
      return status == HttpStatus.OK;

    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}

