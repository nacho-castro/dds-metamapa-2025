package ar.utn.ba.ddsi.fuenteProxy.services;

import ar.utn.ba.ddsi.fuenteProxy.models.dtos.output.HechoDTOOutput;
import java.util.List;

//LE INDICO API A USAR
public interface IProxyService {
  public List<HechoDTOOutput> getHechos(String api);
  public HechoDTOOutput getHechoById(String api, Long id);
}

/*
Strategy:
Seleccionar dinámicamente cuál fuente usar (Disilab, MetaMapa…),
por ejemplo, según un parámetro de entrada, o configuración.
*/
