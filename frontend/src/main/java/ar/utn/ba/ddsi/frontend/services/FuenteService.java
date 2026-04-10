package ar.utn.ba.ddsi.frontend.services;

import ar.utn.ba.ddsi.frontend.dto.input.EditFuenteDTO;
import ar.utn.ba.ddsi.frontend.dto.input.FuenteDTOInput;
import ar.utn.ba.ddsi.frontend.dto.output.ColeccionDTOOutput;
import ar.utn.ba.ddsi.frontend.dto.output.FuenteDTOOutput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;

@Service
public class FuenteService {
  private final WebClient webClient;

  public FuenteService(@Value("${agregador.api.url}") String agregadorServiceUrl) {
    this.webClient = WebClient.builder()
        .baseUrl(agregadorServiceUrl) //base URL
        .build();
  }

  public List<FuenteDTOOutput> obtenerFuentes() {
    try {
      System.out.println("=== LLAMANDO A API DE FUENTES ===");
      System.out.println("URL: " + webClient.toString() + "/fuentes");
      List<FuenteDTOOutput> respuesta = webClient
          .get()
          .uri("/fuentes")
          .retrieve()
          .bodyToMono(new ParameterizedTypeReference<List<FuenteDTOOutput>>() {
          })
          .block();

      System.out.println("=== RESPUESTA DE API FUENTES ===");
      System.out.println("Respuesta: " + (respuesta != null ? "OK" : "NULL"));
      System.out.println("Cantidad: " + (respuesta != null ? respuesta.size() : 0));

      return respuesta != null ? respuesta : new ArrayList<>();

    } catch (Exception e) {
      e.printStackTrace();
      System.err.println("❌ ERROR al obtener fuentes desde API: " + e.getMessage());
      return new ArrayList<>();
    }
  }

  public FuenteDTOOutput crearFuente(FuenteDTOInput dto, String token) {
    try {
      return webClient
          .post()
          .uri("/fuentes")
          .header("Authorization", "Bearer " + token)
          .bodyValue(dto)
          .retrieve()
          .bodyToMono(FuenteDTOOutput.class)
          .block();
    } catch (WebClientResponseException e) {
      throw new RuntimeException("Error en el servicio agregador: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RuntimeException("Error de conexión con el servicio agregador: " + e.getMessage(), e);
    }
  }

  public ColeccionDTOOutput eliminarFuente(Long fuenteId, String token) {
    try {
      return webClient
          .delete()
          .uri("/fuentes/{fuenteId}", fuenteId)
          .header("Authorization", "Bearer " + token)
          .retrieve()
          .bodyToMono(ColeccionDTOOutput.class)
          .block();
    } catch (WebClientResponseException e) {
      throw new RuntimeException("Error en el servicio agregador: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RuntimeException("Error de conexión con el servicio agregador: " + e.getMessage(), e);
    }
  }

  public FuenteDTOOutput editarFuente(Long id, EditFuenteDTO dto, String token) {
    try {
      return webClient
          .put()
          .uri("/fuentes/{id}", id)
          .header("Authorization", "Bearer " + token)
          .bodyValue(dto)
          .retrieve()
          .bodyToMono(FuenteDTOOutput.class)
          .block();

    } catch (WebClientResponseException e) {
      throw new RuntimeException(
          "Error en el servicio agregador al editar la fuente: " + e.getResponseBodyAsString(), e
      );
    } catch (Exception e) {
      throw new RuntimeException(
          "Error de conexión con el servicio agregador al editar la fuente: " + e.getMessage(), e
      );
    }


  }
}
