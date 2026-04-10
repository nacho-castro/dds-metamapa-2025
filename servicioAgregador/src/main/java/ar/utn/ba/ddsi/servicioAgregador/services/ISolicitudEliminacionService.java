package ar.utn.ba.ddsi.servicioAgregador.services;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.SolicitudEliminacionInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.PaginacionResponseDTO;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.SolicitudEliminacionDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.SolicitudEliminacion;

import java.util.List;

public interface ISolicitudEliminacionService {
  public PaginacionResponseDTO<SolicitudEliminacionDTOOutput> buscarPaginado(int page, int limit);
  public void aprobarSolicitudEliminacion(Long idSolicitud);
  public void denegarSolicitudEliminacion(Long idSolicitud);
  public void revisarSolicitud(SolicitudEliminacion solicitud);
  public SolicitudEliminacionDTOOutput crearSolicitud(SolicitudEliminacionInput solicitud);
}
