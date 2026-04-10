package ar.utn.ba.ddsi.servicioAgregador.models.repositories.daos;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;

import java.util.List;

public interface IHechoDao {
  public List<Hecho> findAll();
  public void save(Hecho hecho);
  public Hecho findById(Long id);
  public Hecho findByTitulo(String titulo);
  public void delete (Hecho hecho);
}
