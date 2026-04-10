package ar.utn.ba.ddsi.fuenteProxy.services.impl;

import ar.utn.ba.ddsi.fuenteProxy.models.dtos.output.HechoDTOOutput;
import ar.utn.ba.ddsi.fuenteProxy.services.IApiStrategy;
import ar.utn.ba.ddsi.fuenteProxy.services.IProxyService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Primary
//esta es la implementación predeterminada de IProxyService
//maneja la estrategia entre adapters (otros @service)
public class ProxyService implements IProxyService {

  //MAPEO NOMBRE DE CADA STRATEGIA
  private Map<String, IApiStrategy> strategies;

  public ProxyService(List<IApiStrategy> lista) {
    this.strategies = lista.stream()
        .collect(Collectors.toMap(IApiStrategy::getNombreApi, s -> s));
  }

  private IApiStrategy getStrategy(String api) {
    IApiStrategy strategy = strategies.get(api.toLowerCase());
    if (strategy == null) {
      throw new IllegalArgumentException("API inexistente: " + api);
    }
    return strategy;
  }

  //CADA ESTRATEGIA CONOCE COMO OBTENER HECHOS DE SU API PARTICULAR
  @Override
  public List<HechoDTOOutput> getHechos(String api) {
    return getStrategy(api).obtenerHechos();
  }

  @Override
  public HechoDTOOutput getHechoById(String api, Long id) {
    return getStrategy(api).obtenerHechoPorId(id);
  }
}
