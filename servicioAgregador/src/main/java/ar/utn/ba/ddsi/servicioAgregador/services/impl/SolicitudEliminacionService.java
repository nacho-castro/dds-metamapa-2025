package ar.utn.ba.ddsi.servicioAgregador.services.impl;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.SolicitudEliminacionInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.ColeccionDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.PaginacionResponseDTO;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.SolicitudEliminacionDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.Coleccion;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.ColeccionMapper;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.Estado;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.ISpamDetector;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.SolicitudEliminacion;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.SolicitudMapper;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.SpamDetector;
import ar.utn.ba.ddsi.servicioAgregador.models.repositories.IHechoRepository;
import ar.utn.ba.ddsi.servicioAgregador.models.repositories.ISolicitudEliminacionRepository;
import ar.utn.ba.ddsi.servicioAgregador.services.ISolicitudEliminacionService;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SolicitudEliminacionService implements ISolicitudEliminacionService {
  private ISolicitudEliminacionRepository repository;
  private final ISpamDetector spamDetector;
  private IHechoRepository hechoRepository;

  public SolicitudEliminacionService(ISolicitudEliminacionRepository eliminacionRepository, IHechoRepository hechoRepository) {
    this.repository = eliminacionRepository;
    this.spamDetector = new SpamDetector();
    this.hechoRepository = hechoRepository;
  }

  @Override
  public PaginacionResponseDTO<SolicitudEliminacionDTOOutput> buscarPaginado(int page, int limit) {
    Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(limit, 1));
    Page<SolicitudEliminacion> pageResult = repository.findAllOrdenadoPorPrioridad(pageable);

    List<SolicitudEliminacionDTOOutput> content = pageResult
        .getContent()
        .stream()
        .map(SolicitudMapper::solicitudToDTO)
        .toList();

    return new PaginacionResponseDTO<>(
        content,
        pageResult.getNumber(),
        pageResult.getSize(),
        pageResult.getTotalElements(),
        pageResult.getTotalPages()
    );
  }

  @Override
  @Transactional // Importante para asegurar integridad
  public void aprobarSolicitudEliminacion(Long idSolicitud) {
    SolicitudEliminacion solicitud = repository.findById(idSolicitud)
        .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con id: " + idSolicitud));

    // 1. Cambiar estado de la solicitud
    solicitud.cambiarEstado(Estado.ACEPTADA);

    // 2. ELIMINAR EL HECHO ASOCIADO
    Hecho hecho = solicitud.getHecho();
    if (hecho != null) {
      hecho.setActivo(false);
      hechoRepository.save(hecho);
    }

    repository.save(solicitud);
  }

  @Override
  public void denegarSolicitudEliminacion(Long idSolicitud) {
    SolicitudEliminacion solicitud = repository.findById(idSolicitud)
        .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con id: " + idSolicitud));
    solicitud.cambiarEstado(Estado.RECHAZADA);
    repository.save(solicitud); // <-- persistir cambio
  }

  @Override
  public SolicitudEliminacionDTOOutput crearSolicitud(SolicitudEliminacionInput solicitudDto) {
    try {
      if (solicitudDto.getIdHecho() == null) {
        throw new IllegalArgumentException("El campo idHecho es obligatorio.");
      }

      Hecho hecho = hechoRepository.findById(solicitudDto.getIdHecho())
              .orElseThrow(() -> new EntityNotFoundException(
                      "Hecho no encontrado con ID: " + solicitudDto.getIdHecho()
              ));

      SolicitudEliminacion soli = SolicitudMapper.dtoToSolicitud(solicitudDto);
      soli.setHecho(hecho);
      soli.setFecha(LocalDateTime.now());
      soli.agregarNuevoEstado(Estado.PENDIENTE);

      this.revisarSolicitud(soli);

      SolicitudEliminacion nueva = this.repository.save(soli);
      return SolicitudMapper.solicitudToDTO(nueva);

    } catch (EntityNotFoundException e) {
      // Manejo de error específico si no existe el hecho
      throw e;
    } catch (DataAccessException e) {
      // Error en capa de persistencia
      throw new RuntimeException("Error de acceso a datos al crear la solicitud", e);
    } catch (Exception e) {
      // Cualquier otro error inesperado
      throw new RuntimeException("Error inesperado al crear la solicitud", e);
  }
}

  //REVISAR SI ES SPAM
  //FUNCION INTERNA
  @Override
  public void revisarSolicitud(SolicitudEliminacion solicitud) {
    if (spamDetector.esSpam(solicitud.getMotivoBorrado())){
      solicitud.cambiarEstado(Estado.RECHAZADA_POR_SPAM);
    }
  }
}
