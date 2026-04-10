package ar.utn.ba.ddsi.servicioAgregador.services.impl;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.HechoDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.HechoDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.PaginacionResponseDTO;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.HechoMapper;
import ar.utn.ba.ddsi.servicioAgregador.models.repositories.IHechoRepository;
import ar.utn.ba.ddsi.servicioAgregador.services.IHechoService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HechoService implements IHechoService {
  @Autowired
  private IHechoRepository hechoRepository;

  @Override
  public PaginacionResponseDTO<HechoDTOOutput> obtenerHechos(int page, int limit, String titulo, String descripcion, LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
    Pageable pageable = PageRequest.of(page, limit);

    Page<Hecho> result = hechoRepository.buscarConFiltros(
        titulo, descripcion, fechaDesde, fechaHasta, pageable
    );

    List<HechoDTOOutput> dtoList = result.getContent()
        .stream()
        .filter(Hecho::getActivo)
        .map(HechoMapper::hechoToDTO)
        .toList();

    return new PaginacionResponseDTO<>(
        dtoList,
        result.getNumber(),
        result.getSize(),
        result.getTotalElements(),
        result.getTotalPages()
    );
  }

  @Override
  public HechoDTOOutput obtenerHechoPorId(Long id) {
    return hechoRepository.findById(id)
        .filter(Hecho::getActivo)
        .map(HechoMapper::hechoToDTO)
        .orElse(null);
  }

  @Override
  @Transactional
  public boolean desactivarHecho(Long id) {
    return hechoRepository.findById(id)
        .filter(Hecho::getActivo)
        .map(h -> {
          h.setActivo(false);
          hechoRepository.save(h);
          return true;
        })
        .orElse(false);
  }

  @Override
  @Transactional
  public HechoDTOOutput actualizarHecho(Long id, HechoDTOInput dto) {
    return hechoRepository.findById(id)
        .filter(Hecho::getActivo) // Solo editamos si está activo
        .map(hecho -> {
          // Actualizamos los campos
          hecho.setTitulo(dto.getTitulo());
          hecho.setDescripcion(dto.getDescripcion());
          hecho.setCategoria(dto.getCategoria());
          // Actualizar fecha si viene en el DTO
          if (dto.getFechaAcontecimiento() != null) {
            hecho.setFechaAcontecimiento(dto.getFechaAcontecimiento());
          }

          // Guardamos
          Hecho hechoGuardado = hechoRepository.save(hecho);

          // Devolvemos el DTO actualizado
          return HechoMapper.hechoToDTO(hechoGuardado);
        })
        .orElseThrow(() -> new EntityNotFoundException("Hecho no encontrado o inactivo con ID: " + id));
  }
}

