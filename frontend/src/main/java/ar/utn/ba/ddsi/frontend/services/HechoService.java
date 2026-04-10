package ar.utn.ba.ddsi.frontend.services;

import ar.utn.ba.ddsi.frontend.dto.HechoDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class HechoService {
  private WebClient webClient;
  private final String dinamicaServiceUrl;
  private final String agregadorApiUrl;

  @Autowired
  public HechoService(@Value("${dinamica.api.url}") String dinamicaServiceUrl, @Value("${agregador.api.url}") String agregadorApiUrl) {
    this.webClient = WebClient.builder().build();
    this.dinamicaServiceUrl = dinamicaServiceUrl;
    this.agregadorApiUrl = agregadorApiUrl;
  }

  public HechoDTO obtenerHechoPorId(Long id) {
    try {
      return webClient.get()
          .uri(agregadorApiUrl + "/hechos/{id}", id) // Asegúrate que tu backend tenga este endpoint GET /hechos/{id}
          .retrieve()
          .bodyToMono(HechoDTO.class)
          .block();
    } catch (Exception e) {
      throw new RuntimeException("Error obteniendo el hecho: " + e.getMessage());
    }
  }

  public HechoDTO crearHecho(HechoDTO hechoDTO) {
    try {
      //HTTP al servicio externo de dinamica
      HechoDTO response = webClient
          .post()
          .uri(dinamicaServiceUrl)
          .bodyValue(hechoDTO)
          .retrieve()
          .bodyToMono(HechoDTO.class)
          .block();
      return response;
    } catch (WebClientResponseException e) {
      // Otros errores HTTP
      throw new RuntimeException("Error en el servicio de fuente dinamica: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RuntimeException("Error de conexión con el servicio de fuente dinamica: " + e.getMessage(), e);
    }
  }

  public HechoDTO editarHecho(Long id, HechoDTO hechoDTO, String token) {
    try {
      return webClient.put()
          // CAMBIO: Apunta al Backend Central (/api/hechos)
          .uri(agregadorApiUrl + "/hechos/{id}", id)
          .header("Authorization", "Bearer " + token)
          .bodyValue(hechoDTO)
          .retrieve()
          .bodyToMono(HechoDTO.class)
          .block();
    } catch (WebClientResponseException e) {
      throw new RuntimeException("Error editando hecho: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RuntimeException("Error de conexión: " + e.getMessage(), e);
    }
  }

  public void eliminarHecho(Long id, String token) {
    try {
      System.out.println("Eliminando hecho ID " + id + " en: " + agregadorApiUrl + "/hechos/" + id);

      webClient.delete()
          // CAMBIO: Apunta al Backend Central (/api/hechos)
          .uri(agregadorApiUrl + "/hechos/{id}", id)
          .header("Authorization", "Bearer " + token)
          .retrieve()
          .toBodilessEntity()
          .block();
    } catch (WebClientResponseException e) {
      throw new RuntimeException("Error eliminando hecho: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RuntimeException("Error de conexión: " + e.getMessage(), e);
    }
  }
}
