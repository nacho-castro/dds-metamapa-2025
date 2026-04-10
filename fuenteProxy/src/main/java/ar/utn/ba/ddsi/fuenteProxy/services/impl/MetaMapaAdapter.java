package ar.utn.ba.ddsi.fuenteProxy.services.impl;

import ar.utn.ba.ddsi.fuenteProxy.models.dtos.input.external.api.ApiHechoDTO;
import ar.utn.ba.ddsi.fuenteProxy.models.dtos.input.external.api.ApiResponseDTO;
import ar.utn.ba.ddsi.fuenteProxy.models.dtos.input.external.disilab.DisilabHechoDTO;
import ar.utn.ba.ddsi.fuenteProxy.models.dtos.input.external.disilab.DisilabResponseDTO;
import ar.utn.ba.ddsi.fuenteProxy.models.dtos.output.HechoDTOOutput;
import ar.utn.ba.ddsi.fuenteProxy.models.dtos.output.LugarDTOOutput;
import ar.utn.ba.ddsi.fuenteProxy.models.entities.hechos.Lugar;
import ar.utn.ba.ddsi.fuenteProxy.services.IApiStrategy;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

/*
Adapter:
Cada API externa tiene su propia estructura
 */

@Service
public class MetaMapaAdapter implements IApiStrategy {
  private WebClient webClient;

  public MetaMapaAdapter(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.baseUrl("https://...").build();
  }

  @Override
  public List<HechoDTOOutput> obtenerHechos() {
    return webClient
        .get()
        .uri("/api/hechos")
        .retrieve()
        .bodyToMono(ApiResponseDTO.class)
        .map(this::mapearLista)
        .block(); // bloquea para convertir en respuesta sincrónica
  }

  @Override
  public HechoDTOOutput obtenerHechoPorId(Long id) {
    return webClient
        .get()
        .uri("/api/hechos/{id}", id)
        .retrieve()
        .bodyToMono(ApiHechoDTO.class)
        .map(this::mapearHecho)
        .block();
  }

  //Adicional con filtros
  public List<HechoDTOOutput> getHechosWithParams(
      String categoria,
      LocalDateTime fechaReporteDesde,
      LocalDateTime fechaReporteHasta,
      LocalDateTime fechaAcontDesde,
      LocalDateTime fechaAcontHasta,
      Lugar ubicacion
  ) {
    return webClient
        .get()
        .uri(uriBuilder -> uriBuilder
            .path("/api/hechos")
            .queryParam("categoría", categoria)
            .queryParam("fecha_reporte_desde", fechaReporteDesde)
            .queryParam("fecha_reporte_hasta", fechaReporteHasta)
            .queryParam("fecha_acontecimiento_desde", fechaAcontDesde)
            .queryParam("fecha_acontecimiento_hasta", fechaAcontHasta)
            .queryParam("ubicacion", ubicacion) // revisá si se serializa bien
            .build())
        .retrieve()
        .bodyToMono(ApiResponseDTO.class)
        .map(this::mapearLista)
        .block();
  }

  @Override
  public String getNombreApi() {
    return "metamapa";
  }

  // Métodos de mapeo
  private List<HechoDTOOutput> mapearLista(ApiResponseDTO dtoExterno) {
    return dtoExterno.getData().stream().map(this::mapearHecho).toList();
  }

  private HechoDTOOutput mapearHecho(ApiHechoDTO dto) {
    return new HechoDTOOutput(
        dto.getTitulo(),
        dto.getDescripcion(),
        dto.getCategoria(),
        dto.getFechaHecho(),
        new LugarDTOOutput(dto.getLatitud(), dto.getLongitud())
    );
  }

}
