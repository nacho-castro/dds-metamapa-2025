package ar.utn.ba.ddsi.servicioAgregador.services;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.FuenteAlt;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;

import java.util.List;

public interface IFuenteProxyService {
  public List<Hecho> obtenerHechos(FuenteAlt fuente);
  public Hecho obtenerHechoPorId(FuenteAlt fuente, Long id);
}

//LA FUENTE PROXY SOLO LEE, NO MODIFICA
//BUSCA HECHOS O POR ID