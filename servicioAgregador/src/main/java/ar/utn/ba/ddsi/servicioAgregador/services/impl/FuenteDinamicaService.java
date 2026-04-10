package ar.utn.ba.ddsi.servicioAgregador.services.impl;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.HechoDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.HechoDinamicoDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.FuenteAlt;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.HechoMapper;
import ar.utn.ba.ddsi.servicioAgregador.services.IFuenteDinamicaService;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FuenteDinamicaService implements IFuenteDinamicaService {
  private WebClient.Builder webClientBuilder;

  public FuenteDinamicaService(WebClient.Builder webClientBuilder) {
    this.webClientBuilder = webClientBuilder;
  }

  @Override
  public List<Hecho> obtenerHechos(FuenteAlt fuente) {
    System.out.println("=== ENTRANDO A obtenerHechos() ===");
    System.out.println(">>> Path de la fuente: " + fuente.getPath());
    System.out.println(">>> ID de la fuente: " + fuente.getId());

    WebClient webClient = webClientBuilder.baseUrl(fuente.getPath()).build();

    try {
      String url = fuente.getPath();
      System.out.println(">>> URL completa a llamar: " + url);

      Mono<List<HechoDTOInput>> response = webClient
          .get()
          .uri(url)
          .retrieve()
          .bodyToFlux(HechoDTOInput.class)
          .collectList();

      List<HechoDTOInput> hechosDTO = response.block();
      System.out.println(">>> Hechos DTO recibidos: " + (hechosDTO != null ? hechosDTO.size() : "NULL"));

      if (hechosDTO != null && !hechosDTO.isEmpty()) {
        System.out.println(">>> Primer hecho: " + hechosDTO.get(0).getTitulo());
      }

      List<Hecho> hechos = HechoMapper.DTOToHecho(hechosDTO);
      System.out.println(">>> Hechos mapeados: " + (hechos != null ? hechos.size() : "NULL"));

      return hechos;
    } catch (Exception e) {
      System.err.println("Error al obtener hechos de la fuente dinámica: " + e.getMessage());
      e.printStackTrace();
      return List.of();
    }
  }

  @Override
  public Hecho subirHecho(FuenteAlt fuente, HechoDinamicoDTOInput hecho, String token){
    WebClient webClient = webClientBuilder.baseUrl(fuente.getPath()).build();

    try {
      String url = fuente.getPath();
      HechoDTOInput hechoCreadoDTO;
      if(token == null){
         hechoCreadoDTO = webClient
            .post()
            .uri(url)
            .bodyValue(hecho)
            .retrieve()
            .bodyToMono(HechoDTOInput.class)
            .block();
      } else {
         hechoCreadoDTO = webClient
            .post()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .bodyValue(hecho)
            .retrieve()
            .bodyToMono(HechoDTOInput.class)
            .block();
      }
      return HechoMapper.DTOToHecho(hechoCreadoDTO);

    } catch (Exception e) {
      System.err.println("Error al subir hecho a la fuente dinámica: " + e.getMessage());
      return null;
    }
  }

  @Override
  public Hecho editarHecho(FuenteAlt fuente, Long id, HechoDinamicoDTOInput hechoNuevo, String token) {
    WebClient webClient = webClientBuilder.baseUrl(fuente.getPath()).build();

    try {
      String url = fuente.getPath() + "/" + id;

      HechoDTOInput hechoActualizadoDTO = webClient
          .put()
          .uri(url)
          .bodyValue(hechoNuevo)
          .retrieve()
          .bodyToMono(HechoDTOInput.class)
          .block();

      return HechoMapper.DTOToHecho(hechoActualizadoDTO);

    } catch (Exception e) {
      System.err.println("Error al editar hecho en la fuente dinámica: " + e.getMessage());
      return null;
    }
  }
}
