package ar.utn.ba.ddsi.servicioAgregador.models.repositories.daos;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.Estado;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.SolicitudEliminacion;

import java.util.List;

public interface SolicitudEliminacionContribuyenteIDao {
  public List<SolicitudEliminacion> findALl();
  public SolicitudEliminacion save(SolicitudEliminacion solicitud);
  public void updateEstado(Long id, Estado nuevoEstado);
  public void delete(Long id);
  public SolicitudEliminacion findById(Long id);
}
