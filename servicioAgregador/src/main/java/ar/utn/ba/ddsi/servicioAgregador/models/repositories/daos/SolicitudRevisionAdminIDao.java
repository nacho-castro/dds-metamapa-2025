package ar.utn.ba.ddsi.servicioAgregador.models.repositories.daos;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.Estado;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.SolicitudRevisionAdmin;

import java.util.List;

public interface SolicitudRevisionAdminIDao {
  public List<SolicitudRevisionAdmin> findALl();
  public void save(SolicitudRevisionAdmin solicitud);
  public void updateEstado(Integer id, Estado nuevoEstado);
  public void delete(Integer id);
  public SolicitudRevisionAdmin findById(Integer id);
}
