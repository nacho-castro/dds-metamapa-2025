package ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.SolicitudEdicionDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.SolicitudEliminacionInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.SolicitudEdicionDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.SolicitudEliminacionDTOOutput;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class SolicitudMapper {
  //CLASE UTILITARIA ENCARGADA DE MAPEAR HECHOS A DTOs

  public static SolicitudEliminacionDTOOutput solicitudToDTO(SolicitudEliminacion solicitud) {
    SolicitudEliminacionDTOOutput dto = new SolicitudEliminacionDTOOutput();
    dto.setId(solicitud.getId());
    dto.setIdHecho(solicitud.getHecho() != null ? solicitud.getHecho().getId() : null);
    dto.setMotivoBorrado(solicitud.getMotivoBorrado());
    Estado estado = solicitud.getEstadoActualidad();
    dto.setEstado(estado != null ? estado.toString() : "SIN_ESTADO");
    dto.setFecha(solicitud.getFecha());
    return dto;
  }

  public static SolicitudEliminacion dtoToSolicitud(SolicitudEliminacionInput dto) {
    SolicitudEliminacion solicitud = new SolicitudEliminacion();
    solicitud.setMotivoBorrado(dto.getMotivoBorrado());
    return solicitud;
}

  // --- Mapeo para listas ---
  public static List<SolicitudEliminacionDTOOutput> solicitudesToDTOs(List<SolicitudEliminacion> solicitudes) {
    return solicitudes.stream()
        .map(SolicitudMapper::solicitudToDTO)
        .collect(Collectors.toList());
  }

  public static List<SolicitudEliminacion> dtosToSolicitudes(List<SolicitudEliminacionInput> dtos) {
    return dtos.stream()
        .map(SolicitudMapper::dtoToSolicitud)
        .collect(Collectors.toList());
  }

  public static SolicitudEdicion dtoToSolicitudEdicion(SolicitudEdicionDTOInput dto, Hecho hecho) {

    SolicitudEdicion solicitud = new SolicitudEdicion();

    solicitud.setHecho(hecho);

    // Mapear campos de la propuesta
    solicitud.setTituloPropuesto(dto.getNuevoTitulo());
    solicitud.setDescripcionPropuesta(dto.getNuevaDescripcion());
    solicitud.setCategoriaPropuesta(dto.getNuevaCategoria());
    solicitud.setMotivo(dto.getMotivo());

    solicitud.setFecha(LocalDateTime.now());

    return solicitud;
  }

  // CONVERTIR ENTIDAD -> DTO
  public static SolicitudEdicionDTOOutput solicitudEdicionToDTO(SolicitudEdicion entity) {
    SolicitudEdicionDTOOutput dto = new SolicitudEdicionDTOOutput();
    dto.setId(entity.getId());

    if (entity.getHecho() != null) {
      dto.setIdHecho(entity.getHecho().getId());
      dto.setTituloHechoOriginal(entity.getHecho().getTitulo());
    }

    dto.setTituloPropuesto(entity.getTituloPropuesto());
    dto.setDescripcionPropuesta(entity.getDescripcionPropuesta());
    dto.setCategoriaPropuesta(entity.getCategoriaPropuesta());
    dto.setMotivo(entity.getMotivo());
    dto.setFecha(entity.getFecha());

    Estado estado = entity.getEstadoActualidad();
    if (estado != null) {
      dto.setEstado(estado.toString());
    } else {
      dto.setEstado("DESCONOCIDO");
    }

    return dto;
  }
}
