package ar.utn.ba.ddsi.servicioAgregador.models.repositories.daos.impl;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.Coleccion;
import ar.utn.ba.ddsi.servicioAgregador.models.repositories.daos.IColeccionDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ColeccionDaoMemory implements IColeccionDao {
  List<Coleccion> colecciones = new ArrayList<>();

  @Override
  public List<Coleccion> findAll() {
    return colecciones;
  }

  @Override
  public void save(Coleccion coleccion) {
    // Buscar si ya existe
    for (int i = 0; i < colecciones.size(); i++) {
      if (colecciones.get(i).getId().equals(coleccion.getId())) {
        // Reemplazar
        colecciones.set(i, coleccion);
        return;
      }
    }
    // Si no existe, agregar
    colecciones.add(coleccion);
  }

  @Override
  public Coleccion findById(Long id) {
    return colecciones.stream().filter(c -> c.getId().equals(id)).findFirst().get();
  }

  @Override
  public void delete(Coleccion coleccion) {
    colecciones.remove(coleccion);
  }
}
