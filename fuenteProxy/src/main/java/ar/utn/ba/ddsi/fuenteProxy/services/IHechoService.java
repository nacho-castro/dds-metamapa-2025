package ar.utn.ba.ddsi.fuenteProxy.services;

import ar.utn.ba.ddsi.fuenteProxy.models.entities.hechos.Hecho;

import java.util.List;

public interface IHechoService {
  public List<Hecho> obtenerHechos();
  public Hecho obtenerHecho(Long id);
}
