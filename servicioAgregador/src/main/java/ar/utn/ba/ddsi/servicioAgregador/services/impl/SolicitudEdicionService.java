package ar.utn.ba.ddsi.servicioAgregador.services.impl;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.SolicitudEdicionDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.PaginacionResponseDTO;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.SolicitudEdicionDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.Estado;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.SolicitudEdicion;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.SolicitudMapper;
import ar.utn.ba.ddsi.servicioAgregador.models.repositories.IHechoRepository;
import ar.utn.ba.ddsi.servicioAgregador.models.repositories.ISolicitudEdicionRepository;
import ar.utn.ba.ddsi.servicioAgregador.services.ISolicitudEdicionService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SolicitudEdicionService implements ISolicitudEdicionService {

  private final ISolicitudEdicionRepository solicitudEdicionRepository;
  private final IHechoRepository hechoRepository;

  public SolicitudEdicionService(ISolicitudEdicionRepository solicitudEdicionRepository,
                                 IHechoRepository hechoRepository) {
    this.solicitudEdicionRepository = solicitudEdicionRepository;
    this.hechoRepository = hechoRepository;
  }

  @Override
  public List<SolicitudEdicionDTOOutput> buscarTodos() {
    var solicitudes = this.solicitudEdicionRepository.findAll();

    // Usamos el Mapper para convertir la lista
    return solicitudes.stream()
        .map(SolicitudMapper::solicitudEdicionToDTO)
        .toList();
  }

  @Override
  public SolicitudEdicionDTOOutput crearSolicitud(SolicitudEdicionDTOInput input) {
    // 1. Buscar el Hecho
    Hecho hecho = hechoRepository.findById(input.getIdHecho())
        .orElseThrow(() -> new EntityNotFoundException("Hecho no encontrado con ID: " + input.getIdHecho()));

    // 2. Convertir DTO a Entidad usando el Mapper
    // (El Mapper y el Constructor de la entidad ya se encargan de poner el estado PENDIENTE)
    SolicitudEdicion solicitud = SolicitudMapper.dtoToSolicitudEdicion(input, hecho);

    // 3. Guardar
    SolicitudEdicion solicitudGuardada = solicitudEdicionRepository.save(solicitud);

    // 4. Retornar DTO de respuesta
    return SolicitudMapper.solicitudEdicionToDTO(solicitudGuardada);
  }


  @Override
  public void revisarSolicitud(SolicitudEdicion solicitud) {
    //if(puede editar) --> Todavia no se puede
    //this.contribuyenteRepository.editar(solicitud);
  }

  @Override
  public PaginacionResponseDTO<SolicitudEdicionDTOOutput> buscarPaginado(int page, int limit) {
    int pageNo = Math.max(page, 0);
    int pageSize = Math.max(limit, 1);

    Pageable pageable = PageRequest.of(pageNo, pageSize);
    Page<SolicitudEdicion> pagina = solicitudEdicionRepository.findAllOrdenadoPorPrioridad(pageable);

    // 3. Convertimos a DTO
    List<SolicitudEdicionDTOOutput> content = pagina.getContent().stream()
        .map(SolicitudMapper::solicitudEdicionToDTO)
        .toList();

    // 4. Retornamos la respuesta paginada
    return new PaginacionResponseDTO<>(
        content,
        pagina.getNumber(),
        pagina.getSize(),
        pagina.getTotalElements(),
        pagina.getTotalPages()
    );
  }

  // --- APROBAR: APLICAR CAMBIOS AL HECHO ---
  @Override
  @Transactional
  public void aprobarSolicitudEdicion(Long idSolicitud) {
    // 1. Buscar la solicitud
    SolicitudEdicion solicitud = solicitudEdicionRepository.findById(idSolicitud)
        .orElseThrow(() -> new EntityNotFoundException("Solicitud de edición no encontrada con ID: " + idSolicitud));

    // 2. Buscar el hecho original asociado
    Hecho hecho = hechoRepository.findById(solicitud.getHecho().getId())
        .orElseThrow(() -> new EntityNotFoundException("El hecho asociado ya no existe"));

    // 3. APLICAR CAMBIOS
    // Título
    if (solicitud.getTituloPropuesto() != null) {
      hecho.setTitulo(solicitud.getTituloPropuesto());
    }

    // Descripción
    if (solicitud.getDescripcionPropuesta() != null) {
      hecho.setDescripcion(solicitud.getDescripcionPropuesta());
    }

    if (solicitud.getCategoriaPropuesta() != null) {
      hecho.setCategoria(solicitud.getCategoriaPropuesta());
    }

    // 4. Actualizar estado de la solicitud
    solicitud.cambiarEstado(Estado.ACEPTADA);

    // 5. Guardar ambos
    hechoRepository.save(hecho);
    solicitudEdicionRepository.save(solicitud);
  }

  // --- DENEGAR ---
  @Override
  public void denegarSolicitudEdicion(Long idSolicitud) {
    SolicitudEdicion solicitud = solicitudEdicionRepository.findById(idSolicitud)
        .orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrada con ID: " + idSolicitud));

    // Simplemente cambiamos el estado
    solicitud.cambiarEstado(Estado.RECHAZADA);

    solicitudEdicionRepository.save(solicitud);
  }
}
