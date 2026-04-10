package ar.utn.ba.ddsi.servicioAgregador.services.impl;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.HechoDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.FuenteAlt;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.HechoMapper;
import ar.utn.ba.ddsi.servicioAgregador.services.IFuenteEstaticaService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class FuenteEstaticaService implements IFuenteEstaticaService {
  private WebClient.Builder webClientBuilder;

  //Inyectamos WebClient.Builder
  public FuenteEstaticaService(WebClient.Builder webClientBuilder) {
    this.webClientBuilder = webClientBuilder;
  }

  @Override
  public List<Hecho> obtenerHechos(FuenteAlt fuente) {
    WebClient webClient = webClientBuilder.baseUrl(fuente.getPath()).build();

    try {
      // La fuente expone y devuelve JSON
      String url = fuente.getPath() + "/" + fuente.getPathInfo(); // ej: "desastres_naturales_argentina.csv"

      Mono<List<HechoDTOInput>> response = webClient
          .get()
          .uri(url)
          .retrieve()
          .bodyToFlux(HechoDTOInput.class)
          .collectList();

      List<HechoDTOInput> hechosDTO = response.block();
      // Convertimos HechoDTOOutput a Hecho de nuestro servicio
      return HechoMapper.DTOToHecho(hechosDTO);

    } catch (Exception e) {
      // En caso de error devolvemos lista vacía o podrías propagar excepción custom
      System.err.println("Error al obtener hechos de la fuente estática: " + e.getMessage());
      return List.of();
    }
  }
}
