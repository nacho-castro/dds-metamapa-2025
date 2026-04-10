package ar.utn.ba.ddsi.servicioAgregador.models.repositories.daos.impl;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.servicioAgregador.models.repositories.daos.IHechoDao;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class HechoDaoMemory implements IHechoDao {
  List<Hecho> hechos = new ArrayList<>();

  @Override
  public List<Hecho> findAll() {
    return hechos;
  }

  @Override
  public void save(Hecho hecho) {
    hechos.add(hecho);
  }

  @Override
  public Hecho findById(Long id) {
    return hechos.stream().filter(h -> h.getId().equals(id)).findFirst().get();
  }

  @Override
  public Hecho findByTitulo(String titulo) {
    return hechos.stream().filter(h -> h.getTitulo().equals(titulo)).findFirst().get();
  }

  @Override
  public void delete (Hecho hecho) {
    hechos.remove(hecho);
  }
}
