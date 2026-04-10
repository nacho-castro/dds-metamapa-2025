package ar.utn.ba.ddsi.servicioAgregador.models.repositories.daos.impl;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.Estado;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.SolicitudRevisionAdmin;
import ar.utn.ba.ddsi.servicioAgregador.models.repositories.daos.SolicitudRevisionAdminIDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Repository;


@Repository
public class SolicitudDaoRevisionAdminMemory implements SolicitudRevisionAdminIDao {

  List<SolicitudRevisionAdmin> solicitudes = new ArrayList<>();

  @Override
  public List<SolicitudRevisionAdmin> findALl() {
    return solicitudes;
  }

  @Override
  public void save(SolicitudRevisionAdmin solicitud) {
    solicitudes.add(solicitud);
  }

  @Override
  public void updateEstado(Integer id, Estado nuevoEstado) {
    SolicitudRevisionAdmin solicitud = this.findById(id);
    solicitud.cambiarEstado(nuevoEstado);
  }

  @Override
  public void delete(Integer id) {
    solicitudes.remove(this.findById(id));
  }

  @Override
  public SolicitudRevisionAdmin findById(Integer id) {
    return solicitudes.stream().filter(s -> Objects.equals(s.getId(), id)).findFirst().orElse(null);
  }
}
