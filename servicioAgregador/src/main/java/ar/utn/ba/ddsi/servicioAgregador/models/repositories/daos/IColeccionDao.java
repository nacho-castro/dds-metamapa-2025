package ar.utn.ba.ddsi.servicioAgregador.models.repositories.daos;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.Coleccion;

import java.util.List;

public interface IColeccionDao {
  public List<Coleccion> findAll();
  public void save(Coleccion coleccion);
  public Coleccion findById(Long id);
  public void delete (Coleccion coleccion);
}
