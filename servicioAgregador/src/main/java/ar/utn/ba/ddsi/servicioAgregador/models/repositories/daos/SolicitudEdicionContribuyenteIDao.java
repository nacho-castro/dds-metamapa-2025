package ar.utn.ba.ddsi.servicioAgregador.models.repositories.daos;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.Estado;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.SolicitudEdicion;

import java.util.List;

public interface SolicitudEdicionContribuyenteIDao {
  public List<SolicitudEdicion> findALl();
  public void save(SolicitudEdicion solicitud);
  public void updateEstado(Integer id, Estado nuevoEstado);
  public void delete(Integer id);
  public SolicitudEdicion findById(Integer id);
}
