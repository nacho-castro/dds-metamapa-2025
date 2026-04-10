package ar.utn.ba.ddsi.frontend.services;

import ar.utn.ba.ddsi.frontend.dto.input.ColeccionDTOInput;
import ar.utn.ba.ddsi.frontend.dto.input.EditColeccionDTO;
import ar.utn.ba.ddsi.frontend.dto.output.ColeccionDTOOutput;
import ar.utn.ba.ddsi.frontend.dto.output.HechoDTOOutput;
import ar.utn.ba.ddsi.frontend.dto.output.PageDTOOutput;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ColeccionService {
  private final WebClient webClient;

  public ColeccionService(@Value("${agregador.api.url}") String agregadorServiceUrl) {
    this.webClient = WebClient.builder()
        .baseUrl(agregadorServiceUrl) //base URL
        .build();
  }

  public PageDTOOutput<ColeccionDTOOutput> obtenerColecciones(int page, int limit, String titulo) {
    try {
      PageDTOOutput<ColeccionDTOOutput> respuesta = webClient
          .get()
          .uri(uriBuilder -> {
            var builder = uriBuilder
                .path("/colecciones")
                .queryParam("page", page)
                .queryParam("limit", limit);

            if (titulo != null && !titulo.isBlank()) {
              builder.queryParam("titulo", titulo);
            }
            return builder.build();
          })
          .retrieve()
          .bodyToMono(new ParameterizedTypeReference<PageDTOOutput<ColeccionDTOOutput>>() {})
          .block();

      return respuesta != null ? respuesta : new PageDTOOutput<>(List.of(), 0, 0, 0, false, false);

    } catch (Exception e) {
      e.printStackTrace();
      return new PageDTOOutput<>(List.of(), 0, 0, 0, false, false);
    }
  }

  public ColeccionDTOOutput obtenerColeccionPorId(Long id) {
    try {
      return webClient
          .get()
          .uri("/colecciones/{id}", id)
          .retrieve()
          .bodyToMono(ColeccionDTOOutput.class)
          .block();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public PageDTOOutput<HechoDTOOutput> obtenerHechosColeccion(
      int page, int limit, Long id, boolean curada,
      String keyword, String categoria, LocalDate fechaInicio, LocalDate fechaFin) {

    try {
      PageDTOOutput<HechoDTOOutput> response = webClient
          .get()
          .uri(uriBuilder -> {
            var builder = uriBuilder
                .path("/colecciones/{id}/hechos")
                .queryParam("page", page)
                .queryParam("limit", limit)
                .queryParam("curada", curada);

            // Agregamos condicionalmente los filtros
            if (keyword != null && !keyword.isBlank()) {
              builder.queryParam("keyword", keyword);
            }
            if (categoria != null && !categoria.isBlank()) {
              builder.queryParam("categoria", categoria);
            }
            if (fechaInicio != null) {
              builder.queryParam("fechaInicio", fechaInicio);
            }
            if (fechaFin != null) {
              builder.queryParam("fechaFin", fechaFin);
            }

            return builder.build(id);
          })
          .retrieve()
          .bodyToMono(new ParameterizedTypeReference<PageDTOOutput<HechoDTOOutput>>() {})
          .block();

      return response != null ? response : new PageDTOOutput<>(List.of(), 0, 0, 0, false, false);

    } catch (Exception e) {
      e.printStackTrace();
      return new PageDTOOutput<>(List.of(), 0, 0, 0, false, false);
    }
  }

  //TODOS LOS HECHOS GENERAL
  // HECHOS CON FILTROS OPCIONALES
  public PageDTOOutput<HechoDTOOutput> obtenerHechos(
      Integer page,
      Integer limit,
      String titulo,
      String descripcion,
      LocalDateTime fechaDesde,
      LocalDateTime fechaHasta
  ) {
    try {
      PageDTOOutput<HechoDTOOutput> response = webClient
          .get()
          .uri(uriBuilder -> {
            var builder = uriBuilder.path("/hechos");

            // Page y limit opcionales
            if (page != null) builder.queryParam("page", page);
            if (limit != null) builder.queryParam("limit", limit);

            // Filtros opcionales
            if (titulo != null && !titulo.isBlank()) {
              builder.queryParam("titulo", titulo);
            }
            if (descripcion != null && !descripcion.isBlank()) {
              builder.queryParam("descripcion", descripcion);
            }
            if (fechaDesde != null) {
              builder.queryParam("fechaDesde", fechaDesde);
            }
            if (fechaHasta != null) {
              builder.queryParam("fechaHasta", fechaHasta);
            }

            return builder.build();
          })
          .retrieve()
          .bodyToMono(new ParameterizedTypeReference<PageDTOOutput<HechoDTOOutput>>() {})
          .block();

      return response != null
          ? response
          : new PageDTOOutput<>(List.of(), 0, 0, 0, false, false);

    } catch (Exception e) {
      e.printStackTrace();
      return new PageDTOOutput<>(List.of(), 0, 0, 0, false, false);
    }
  }

  public ColeccionDTOOutput crearColeccion(ColeccionDTOInput coleccionDTOInput, String token) {
    try {
      return webClient
          .post()
          .uri("/colecciones")
          .header("Authorization", "Bearer " + token)
          .bodyValue(coleccionDTOInput)
          .retrieve()
          .bodyToMono(ColeccionDTOOutput.class)
          .block();
    } catch (WebClientResponseException e) {
      throw new RuntimeException("Error en el servicio agregador: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RuntimeException("Error de conexión con el servicio agregador: " + e.getMessage(), e);
    }
  }

  public ColeccionDTOOutput editarColeccion(Long id, EditColeccionDTO dto, String token) {
    try {
      return webClient
          .put()
          .uri("/colecciones/{id}", id)
          .header("Authorization", "Bearer " + token)
          .bodyValue(dto)
          .retrieve()
          .bodyToMono(ColeccionDTOOutput.class)
          .block();
    } catch (WebClientResponseException e) {
      throw new RuntimeException("Error en el servicio agregador: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RuntimeException("Error de conexión con el servicio agregador: " + e.getMessage(), e);
    }
  }

  public ColeccionDTOOutput eliminarColeccion(Long coleccionId, String token) {
    try {
      return webClient
          .delete()
          .uri("/colecciones/{coleccionId}", coleccionId)
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

  public ColeccionDTOOutput agregarFuente(Long idColeccion, Long idFuente, String token) {
    try {
      return webClient
          .post()
          .uri("/colecciones/{idColeccion}/fuentes/{idFuente}", idColeccion, idFuente)
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
}
