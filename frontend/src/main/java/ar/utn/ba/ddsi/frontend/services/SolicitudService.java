package ar.utn.ba.ddsi.frontend.services;

import ar.utn.ba.ddsi.frontend.dto.input.SolicitudEdicionDTOInput;
import ar.utn.ba.ddsi.frontend.dto.input.SolicitudInputDTO;
import ar.utn.ba.ddsi.frontend.dto.output.SolicitudEdicionDTOOutput;
import java.util.List;

import ar.utn.ba.ddsi.frontend.dto.output.PageDTOOutput;
import ar.utn.ba.ddsi.frontend.dto.output.SolicitudAdminDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class SolicitudService {

  private static final Logger log = LoggerFactory.getLogger(SolicitudService.class);
  private WebClient webClient;

  @Autowired
  public SolicitudService(@Value("${agregador.api.url}") String agregadorServiceUrl) {
    this.webClient = WebClient.builder()
        .baseUrl(agregadorServiceUrl)
        .build();
  }

  // ------------------------
  // CREAR SOLICITUD
  // ------------------------
  public SolicitudAdminDTO crearSolicitud(SolicitudInputDTO solicitudInputDTO, String token) {
    try {
      log.info("Creando solicitud de eliminación: {}", solicitudInputDTO);

      return webClient.post()
          .uri("/solicitudes/eliminacion")
          .header("Authorization", "Bearer " + token) // <--- ¡ESTA LÍNEA ES LA CLAVE!
          .bodyValue(solicitudInputDTO)
          .retrieve()
          .bodyToMono(SolicitudAdminDTO.class)
          .block();

    } catch (WebClientResponseException e) {
      throw new RuntimeException("Error en el servicio agregador: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RuntimeException("Error de conexión con el servicio agregador: " + e.getMessage(), e);
    }
  }

  // ------------------------
  // OBTENER SOLICITUDES
  // ------------------------
  public PageDTOOutput<SolicitudAdminDTO> obtenerSolicitudesEliminacion(int page, int limit, String estado) {

    try {
      return webClient.get()
          .uri(uriBuilder -> {
            var builder = uriBuilder
                .path("/solicitudes/eliminacion")
                .queryParam("page", page)
                .queryParam("limit", limit);

            if (estado != null && !estado.isBlank()) {
              builder.queryParam("estado", estado);
            }

            return builder.build();
          })
          .retrieve()
          .bodyToMono(new ParameterizedTypeReference<PageDTOOutput<SolicitudAdminDTO>>() {})
          .block();

    } catch (Exception e) {
      log.error("Error obteniendo solicitudes de eliminación", e);
      return new PageDTOOutput<>(
          List.of(),
          0,
          limit,
          0,
          false,
          false
      );
    }
  }

  // APROBAR
  public void aprobarSolicitud(Long id, String token) { // <--- Agregar token
    try {
      webClient.patch() // Asegúrate que tu backend espere PATCH
          .uri("/solicitudes/eliminacion/{id}", id)
          .header("Authorization", "Bearer " + token) // <--- Header Auth
          .retrieve()
          .toBodilessEntity()
          .block();
    } catch (Exception e) {
      throw new RuntimeException("Error al aprobar solicitud: " + e.getMessage());
    }
  }

  // DENEGAR
  public void denegarSolicitud(Long id, String token) { // <--- Agregar token
    try {
      webClient.delete() // Asegúrate que tu backend espere DELETE
          .uri("/solicitudes/eliminacion/{id}", id)
          .header("Authorization", "Bearer " + token) // <--- Header Auth
          .retrieve()
          .toBodilessEntity()
          .block();
    } catch (Exception e) {
      throw new RuntimeException("Error al denegar solicitud: " + e.getMessage());
    }
  }

  public void crearSolicitudEdicion(SolicitudEdicionDTOInput dto, String token) {
    try {
      webClient.post()
          .uri("/solicitudes/edicion")
          .header("Authorization", "Bearer " + token)
          .bodyValue(dto)
          .retrieve()
          .bodyToMono(Void.class) // Asumimos que no devuelve nada o no nos importa
          .block();
    } catch (Exception e) {
      throw new RuntimeException("Error creando solicitud de edición: " + e.getMessage());
    }
  }

  // Obtener lista de solicitudes de edición
  public PageDTOOutput<SolicitudEdicionDTOOutput> obtenerSolicitudesEdicion(int page, int limit) {
    try {
      return webClient.get()
          .uri(uriBuilder -> uriBuilder
              .path("/solicitudes/edicion") // Ruta del backend
              .queryParam("page", page)
              .queryParam("limit", limit)
              .build())
          .retrieve()
          .bodyToMono(new ParameterizedTypeReference<PageDTOOutput<SolicitudEdicionDTOOutput>>() {})
          .block();
    } catch (Exception e) {
      log.error("Error obteniendo solicitudes de edición", e);
      return new PageDTOOutput<>(List.of(), 0, limit, 0, false, false);
    }
  }

  // Aprobar Edición
  public void aprobarEdicion(Long id, String token) {
    webClient.patch()
        .uri("/solicitudes/edicion/{id}", id)
        .header("Authorization", "Bearer " + token)
        .retrieve()
        .toBodilessEntity()
        .block();
  }

  // Denegar Edición
  public void denegarEdicion(Long id, String token) {
    webClient.delete()
        .uri("/solicitudes/edicion/{id}", id)
        .header("Authorization", "Bearer " + token)
        .retrieve()
        .toBodilessEntity()
        .block();
  }
}
