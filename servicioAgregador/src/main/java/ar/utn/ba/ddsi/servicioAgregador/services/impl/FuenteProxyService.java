package ar.utn.ba.ddsi.servicioAgregador.services.impl;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.HechoDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.FuenteAlt;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.HechoMapper;
import ar.utn.ba.ddsi.servicioAgregador.services.IFuenteProxyService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FuenteProxyService implements IFuenteProxyService {
  private WebClient.Builder webClientBuilder;

  public FuenteProxyService(WebClient.Builder webClientBuilder) {
    this.webClientBuilder = webClientBuilder;
  }

  @Override
  public List<Hecho> obtenerHechos(FuenteAlt fuente) {
    WebClient webClient = webClientBuilder.baseUrl(fuente.getPath()).build();
    try {
      // La fuente expone y devuelve JSON
      String url = fuente.getPath() + "/" + fuente.getPathInfo();

      Mono<List<HechoDTOInput>> response = webClient
          .get()
          .uri(url)
          .retrieve()
          .bodyToFlux(HechoDTOInput.class)
          .collectList();

      List<HechoDTOInput> hechosDTO = response.block();
      return HechoMapper.DTOToHecho(hechosDTO);

    } catch (Exception e) {
      System.err.println("Error al obtener hechos de la fuente proxy: " + e.getMessage());
      return List.of();
    }
  }

  @Override
  public Hecho obtenerHechoPorId(FuenteAlt fuente, Long id) {
    WebClient webClient = webClientBuilder.baseUrl(fuente.getPath()).build();
    try {
      // La fuente expone y devuelve JSON
      String url = fuente.getPath() + "/" + fuente.getPathInfo() + "/" + id;

      HechoDTOInput hechoDTO = webClient
          .get()
          .uri(url)
          .retrieve()
          .bodyToMono(HechoDTOInput.class)
          .block();  // Bloquea hasta obtener el objeto

      return HechoMapper.DTOToHecho(hechoDTO);

    } catch (Exception e) {
      System.err.println("Error al obtener hecho de la fuente proxy: " + e.getMessage());
      return null; // Devuelvo null
    }
  }
}
