package ar.utn.ba.ddsi.fuenteDinamica.services;

import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Hecho;

import java.util.List;

public interface IHechoService {
  public Hecho crearHecho(Hecho hecho);
  public List<Hecho> obtenerHechos();
  public Hecho obtenerHechoPorId(Long id);
  public Hecho editarHecho(Long id, Hecho hechoNuevo);
  public Hecho borrarHecho(Long id);
}
