package ar.utn.ba.ddsi.servicioAgregador.services;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.SolicitudEdicionDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.PaginacionResponseDTO;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.SolicitudEdicionDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.SolicitudEdicion;

import java.util.List;

public interface ISolicitudEdicionService {
  public List<SolicitudEdicionDTOOutput> buscarTodos();
  public void revisarSolicitud(SolicitudEdicion solicitud);
  public SolicitudEdicionDTOOutput crearSolicitud(SolicitudEdicionDTOInput input);
  public PaginacionResponseDTO<SolicitudEdicionDTOOutput> buscarPaginado(int page, int limit);
  public void aprobarSolicitudEdicion(Long idSolicitud);
  public void denegarSolicitudEdicion(Long idSolicitud);
}
