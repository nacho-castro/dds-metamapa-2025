package ar.utn.ba.ddsi.servicioAgregador.models.repositories.daos.impl;


import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.Estado;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.SolicitudEdicion;
import ar.utn.ba.ddsi.servicioAgregador.models.repositories.daos.SolicitudEdicionContribuyenteIDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Repository;

@Repository
public class SolicitudDaoEdicionContribuyenteMemory implements SolicitudEdicionContribuyenteIDao {
  List<SolicitudEdicion> solicitudes = new ArrayList<>();

  @Override
  public List<SolicitudEdicion> findALl() {
    return solicitudes;
  }

  @Override
  public void save(SolicitudEdicion solicitud) {
    solicitudes.add(solicitud);
  }

  @Override
  public void updateEstado(Integer id, Estado nuevoEstado) {
    SolicitudEdicion solicitud = this.findById(id);
    solicitud.cambiarEstado(nuevoEstado);
  }

  @Override
  public void delete(Integer id) {
    solicitudes.remove(this.findById(id));
  }

  @Override
  public SolicitudEdicion findById(Integer id) {
    return solicitudes.stream().filter(s -> Objects.equals(s.getId(), id)).findFirst().orElse(null);
  }
}
