package ar.utn.ba.ddsi.servicioAgregador.services;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.SolicitudAdminDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.SolicitudRevisionAdmin;

import java.util.List;

public interface ISolicitudRevisionAdminService {
  public List<SolicitudAdminDTOOutput> buscarTodos();
  public void revisarSolicitud(SolicitudRevisionAdmin solicitud);
}
