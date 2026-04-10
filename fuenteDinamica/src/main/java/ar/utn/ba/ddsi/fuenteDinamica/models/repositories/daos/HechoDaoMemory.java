package ar.utn.ba.ddsi.fuenteDinamica.models.repositories.daos;

import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Hecho;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class HechoDaoMemory implements HechoIDao{
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
    return hechos.stream()
        .filter(h -> h.getId().equals(id))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Hecho con ID " + id + " no encontrado"));
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
