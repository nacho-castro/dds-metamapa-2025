package ar.utn.ba.ddsi.fuenteProxy.services.impl;

import ar.utn.ba.ddsi.fuenteProxy.models.dtos.input.external.disilab.DisilabHechoDTO;
import ar.utn.ba.ddsi.fuenteProxy.models.dtos.input.external.disilab.DisilabResponseDTO;
import ar.utn.ba.ddsi.fuenteProxy.models.dtos.input.external.disilab.LoginDTO;
import ar.utn.ba.ddsi.fuenteProxy.models.dtos.output.HechoDTOOutput;
import ar.utn.ba.ddsi.fuenteProxy.models.dtos.output.LugarDTOOutput;
import ar.utn.ba.ddsi.fuenteProxy.services.IApiStrategy;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

/*
Adapter:
Cada API externa tiene su propia estructura
 */
@Service
public class DisilabAdapter implements IApiStrategy {

  private final WebClient webClient;

  @Value("${disilab.api.base-url}")
  private String baseUrl;

  @Value("${disilab.api.username}")
  private String username;

  @Value("${disilab.api.password}")
  private String password;

  public DisilabAdapter(WebClient.Builder builder) {
    this.webClient = builder.build();
  }

  @Override
  public List<HechoDTOOutput> obtenerHechos() {
    String token = obtenerToken();

    int page = 1;
    int lastPage = 1;
    List<HechoDTOOutput> acumulado = new ArrayList<>();

    do {
      String url = baseUrl + "/desastres?page=" + page;

      DisilabResponseDTO respuesta = webClient.get()
          .uri(url)
          .header("Authorization", "Bearer " + token)
          .retrieve()
          .bodyToMono(DisilabResponseDTO.class)
          .block();

      if (respuesta == null || respuesta.getData() == null)
        break;

      acumulado.addAll(
          respuesta.getData().stream()
              .map(this::mapearHecho)
              .toList()
      );

      lastPage = respuesta.getLast_page();
      page++;

    } while (page <= lastPage);

    return acumulado;
  }

  @Override
  public HechoDTOOutput obtenerHechoPorId(Long id) {
    String token = obtenerToken();

    String url = baseUrl + "/desastres/" + id;

    DisilabHechoDTO respuesta = webClient.get()
        .uri(url)
        .header("Authorization", "Bearer " + token)
        .retrieve()
        .bodyToMono(DisilabHechoDTO.class)
        .block();

    return mapearHecho(respuesta);
  }

  @Override
  public String getNombreApi() {
    return "disilab";
  }

  public String obtenerToken() {
    String url = baseUrl + "/login";

    return webClient.post()
        .uri(url)
        .bodyValue(new LoginDTO(username, password))
        .retrieve()
        .bodyToMono(String.class)
        .map(json -> {
          try {
            JsonNode root = new ObjectMapper().readTree(json);
            return root.path("data").path("access_token").asText();
          } catch (Exception e) {
            throw new RuntimeException("Error al parsear el token", e);
          }
        })
        .block();
  }

  private HechoDTOOutput mapearHecho(DisilabHechoDTO dto) {
    return new HechoDTOOutput(
        dto.getTitulo(),
        dto.getDescripcion(),
        dto.getCategoria(),
        dto.getFechaHecho(),
        new LugarDTOOutput(dto.getLatitud(), dto.getLongitud())
    );
  }
}
