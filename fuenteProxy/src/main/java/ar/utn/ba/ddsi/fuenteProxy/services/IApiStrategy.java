package ar.utn.ba.ddsi.fuenteProxy.services;

import ar.utn.ba.ddsi.fuenteProxy.models.dtos.output.HechoDTOOutput;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IApiStrategy {
  List<HechoDTOOutput> obtenerHechos();
  HechoDTOOutput obtenerHechoPorId(Long id);
  String getNombreApi();
}

/*
Adapter:
Cada API externa tiene su propia estructura (recibo DTOS externos),
necesito adaptar su respuesta a mi modelo interno (HechoDTOOutput).

Strategy:
Seleccionar dinámicamente cuál fuente usar (Disilab, MetaMapa…),
por ejemplo, según un parámetro de entrada, o configuración.
 */
