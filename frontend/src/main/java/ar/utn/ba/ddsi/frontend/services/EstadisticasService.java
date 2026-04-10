package ar.utn.ba.ddsi.frontend.services;

import ar.utn.ba.ddsi.frontend.dto.EstadisticasDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EstadisticasService {

  private final WebClient webClient;
  private final String estadisticasApiUrl;

  @Autowired
  public EstadisticasService(@Value("${estadisticas.api.url}") String estadisticasApiUrl) {
    this.webClient = WebClient.builder().build();
    this.estadisticasApiUrl = estadisticasApiUrl;
  }

  // Obtener estadísticas por tipo
  public List<EstadisticasDTO> obtenerPorTipo(String tipo) {
    try {
      List<EstadisticasDTO> lista = webClient.get()
          .uri(estadisticasApiUrl + "/estadisticas/{tipo}", tipo)
          .retrieve()
          .bodyToFlux(EstadisticasDTO.class)
          .collectList()
          .block();

      return lista != null ? lista : List.of();
    } catch (Exception e) {
      e.printStackTrace();
      return List.of();
    }
  }

  // Recalcular estadísticas por colección
  public void recalcularTodasPorColeccion(Long coleccionId, List<String> categorias) {
    try {
      webClient.post()
          .uri(estadisticasApiUrl + "/estadisticas/recalcular/coleccion/{coleccionId}", coleccionId)
          .bodyValue(categorias)
          .retrieve()
          .toBodilessEntity()
          .block();
    } catch (Exception e) {
      throw new RuntimeException("Error al recalcular estadísticas: " + e.getMessage(), e);
    }
  }

  // Obtener estadísticas por colección
  public List<EstadisticasDTO> obtenerPorColeccion(Long coleccionId) {
    try {
      List<EstadisticasDTO> lista = webClient.get()
          .uri(estadisticasApiUrl + "/estadisticas/coleccion/{coleccionId}", coleccionId)
          .retrieve()
          .bodyToFlux(EstadisticasDTO.class)
          .collectList()
          .block();

      return lista != null ? lista : List.of();
    } catch (Exception e) {
      e.printStackTrace();
      return List.of();
    }
  }
}
