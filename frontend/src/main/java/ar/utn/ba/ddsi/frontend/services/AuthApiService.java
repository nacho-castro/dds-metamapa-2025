package ar.utn.ba.ddsi.frontend.services;

import ar.utn.ba.ddsi.frontend.dto.input.AuthResponseDTO;
import ar.utn.ba.ddsi.frontend.dto.input.RolesPermisosDTO;
import ar.utn.ba.ddsi.frontend.dto.output.LoginRequest;
import ar.utn.ba.ddsi.frontend.services.internal.WebApiCallerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/*
Servicio intermedio que va a encargarse de las request http
a nuestro servicio externo de autenticacion
*/

@Service
public class AuthApiService {
  private static final Logger log = LoggerFactory.getLogger(AuthApiService.class);
  private final WebClient webClient;
  private final WebApiCallerService webApiCallerService;
  private final String authServiceUrl;
  private final ObjectMapper objectMapper;

  @Autowired
  public AuthApiService(WebApiCallerService webApiCallerService,
                        @Value("${auth.api.url}") String authServiceUrl) {
    this.webClient = WebClient.builder()
        .baseUrl(authServiceUrl)
        .build();
    this.webApiCallerService = webApiCallerService;
    this.authServiceUrl = authServiceUrl;
    this.objectMapper = new ObjectMapper();
  }

  public AuthResponseDTO login(LoginRequest loginRequest) {
    try {
      AuthResponseDTO response = webClient
          .post()
          .uri("/auth")
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(loginRequest)
          .retrieve()
          .bodyToMono(AuthResponseDTO.class)
          .block();

      log.info("Login successful for user: {}", loginRequest.getUsername());
      return response;

    } catch (WebClientResponseException e) {
      log.error("Error HTTP {}: {}", e.getStatusCode().value(), e.getResponseBodyAsString());

      if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
        log.warn("Invalid credentials for user: {}", loginRequest.getUsername());
        return null;
      }

      throw new RuntimeException("Error en el servicio de autenticación: " + e.getMessage(), e);

    } catch (Exception e) {
      log.error("Connection error with authentication service at {}", authServiceUrl, e);
      throw new RuntimeException("Error de conexión con el servicio de autenticación: " + e.getMessage(), e);
    }
  }

  public RolesPermisosDTO getRolesPermisos(String accessToken) {
    try {
      RolesPermisosDTO response = webClient
          .get()
          .uri("/auth/user/roles-permisos")
          .header("Authorization", "Bearer " + accessToken)
          .retrieve()
          .bodyToMono(RolesPermisosDTO.class)
          .block();
      return response;

    } catch (Exception e) {
      log.error(e.getMessage());
      throw new RuntimeException("Error al obtener roles y permisos: " + e.getMessage(), e);
    }
  }
}
