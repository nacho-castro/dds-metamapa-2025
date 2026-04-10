package ar.edu.utn.frba.dds.tpa.estadisticas.client;

import ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.ColeccionDTO;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.PageResponse;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.SolicitudDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.externalDTO.ColeccionDTOInput;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.externalDTO.HechoDTOInput;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.externalDTO.SolicitudEliminacionInput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class AgregadorClient {

  private final WebClient webClient;
  private final AgregadorMapper mapper;

  public AgregadorClient(AgregadorMapper mapper, @Value("${agregador.api.url}") String agregadorApiUrl) {
    this.webClient = WebClient.builder()
        .baseUrl(agregadorApiUrl)
        .build();
    this.mapper = mapper;
  }

  //TRAER COLECCIONES (con Hechos)
  public List<ColeccionDTO> obtenerColecciones() {
    List<ColeccionDTOInput> colecciones = obtenerTodoPaginado(page ->
        webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/colecciones")
                .queryParam("page", page)
                .queryParam("limit", 100)
                .build())
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<PageResponse<ColeccionDTOInput>>() {})
            .block()
    );

    return colecciones.stream()
        .filter(co -> co.getId() != null)
        .map(co -> {
          List<HechoDTOInput> hechos = obtenerHechosPorColeccionDTO(co.getId());
          return mapper.mapToColeccionDTO(co, hechos);
        })
        .toList();
  }

  //TRAE HECHOS INPUT DE COLECCION
  public List<HechoDTOInput> obtenerHechosPorColeccionDTO(Long coleccionId) {
    List<HechoDTOInput> hechos = obtenerTodoPaginado(page ->
        webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/colecciones/{id}/hechos")
                .queryParam("page", page)
                .queryParam("limit", 100)
                .build(coleccionId))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<PageResponse<HechoDTOInput>>() {})
            .block()
    );

    return hechos != null ? hechos : Collections.emptyList();
  }

  public List<SolicitudDTO> obtenerSolicitudesEliminacion() {
    List<SolicitudEliminacionInput> solicitudes = obtenerTodoPaginado(page ->
        webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/solicitudes/eliminacion")
                .queryParam("page", page)
                .queryParam("limit", 100)
                .build())
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<PageResponse<SolicitudEliminacionInput>>() {})
            .block()
    );

    return solicitudes.stream()
        .map(mapper::mapToSolicitudDTO)
        .toList();
  }

  private <T> List<T> obtenerTodoPaginado(Function<Integer, PageResponse<T>> pageSupplier) {
    List<T> resultados = new ArrayList<>();
    int page = 0;

    while (true) {
      PageResponse<T> response = pageSupplier.apply(page);

      if (response == null || response.getContent() == null || response.getContent().isEmpty()) {
        break;
      }

      resultados.addAll(response.getContent());

      if (!response.isHasNext()) {
        break;
      }
      page++;
    }
    return resultados;
  }

}