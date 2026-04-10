package ar.utn.ba.ddsi.servicioAgregador.models.repositories.daos.impl;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.Estado;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.SolicitudEliminacion;
import ar.utn.ba.ddsi.servicioAgregador.models.repositories.daos.SolicitudEliminacionContribuyenteIDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Repository;


@Repository
public class SolicitudDaoEliminacionContribuyenteMemory implements SolicitudEliminacionContribuyenteIDao {

  List<SolicitudEliminacion> solicitudes = new ArrayList<>();

  @Override
  public List<SolicitudEliminacion> findALl() {
    return solicitudes;
  }

  @Override
  public SolicitudEliminacion save(SolicitudEliminacion solicitud) {
    solicitudes.add(solicitud);
    return solicitud;
  }

  @Override
  public void updateEstado(Long id, Estado nuevoEstado) {
    SolicitudEliminacion solicitud = this.findById(id);
    solicitud.cambiarEstado(nuevoEstado);
  }

  @Override
  public void delete(Long id) {
    solicitudes.remove(this.findById(id));
  }

  @Override
  public SolicitudEliminacion findById(Long id) {
      return solicitudes.stream().filter(s -> Objects.equals(s.getId(), id)).findFirst().orElse(null);
  }
}
