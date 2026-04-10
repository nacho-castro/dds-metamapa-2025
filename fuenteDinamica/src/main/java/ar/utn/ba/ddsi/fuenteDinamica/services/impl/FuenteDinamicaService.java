package ar.utn.ba.ddsi.fuenteDinamica.services.impl;

import ar.utn.ba.ddsi.fuenteDinamica.models.dtos.input.HechoDTOInput;
import ar.utn.ba.ddsi.fuenteDinamica.models.dtos.output.HechoDTOOutput;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.HechoMapper;
import ar.utn.ba.ddsi.fuenteDinamica.services.IFuenteDinamicaService;
import ar.utn.ba.ddsi.fuenteDinamica.services.IHechoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FuenteDinamicaService implements IFuenteDinamicaService {

  private IHechoService hechoService;

  public FuenteDinamicaService(IHechoService hechoService) {
    this.hechoService = hechoService;
  }

  @Override
  public HechoDTOOutput subirHecho(HechoDTOInput hechoInput, Long idUsuario) {
    // Crear entidad desde el DTO
    Hecho hecho = HechoMapper.DTOToHecho(hechoInput);

    if (idUsuario == null) {
      hecho.setEditable(false);
    } else {
      hecho.setUsuarioId(idUsuario);
      hecho.setEditable(true);
    }

    hecho.setActivo(true);
    hecho.setFechaCarga(LocalDateTime.now());
    Hecho hechoCreado = hechoService.crearHecho(hecho);
    return HechoMapper.hechoToDTO(hechoCreado);
  }

  @Override
  public List<HechoDTOOutput> obtenerHechos() {
    return hechoService
        .obtenerHechos()
        .stream()
        .map(HechoMapper::hechoToDTO)
        .toList();
  }

  @Override
  public HechoDTOOutput buscarHechoPorId(Long id) {
    Hecho hecho = hechoService.obtenerHechoPorId(id);
    return HechoMapper.hechoToDTO(hecho);
  }

  @Override
  public HechoDTOOutput editarHecho(Long id, HechoDTOInput hechoNuevo, Long idUsuario) {
    if (idUsuario == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido o ausente");
    }

    Hecho hechoExistente = hechoService.obtenerHechoPorId(id);

    if (hechoExistente.getUsuarioId() == null) {
      throw new ResponseStatusException(
          HttpStatus.FORBIDDEN,
          "Este hecho no puede ser editado"
      );
    }

    if (!hechoExistente.getUsuarioId().equals(idUsuario)) {
      throw new ResponseStatusException(
          HttpStatus.FORBIDDEN,
          "No tenés permiso para editar este hecho"
      );
    }

    if (!hechoExistente.isEditable()) {
      throw new ResponseStatusException(
          HttpStatus.FORBIDDEN,
          "Este hecho no es editable"
      );
    }

    Hecho hechoActualizado = HechoMapper.DTOToHecho(hechoNuevo);

    hechoService.editarHecho(id, hechoActualizado);

    return HechoMapper.hechoToDTO(hechoActualizado);
  }

  @Override
  public HechoDTOOutput eliminarHecho(Long id) {
    Hecho hecho = hechoService.borrarHecho(id);
    return HechoMapper.hechoToDTO(hecho);
  }

}
