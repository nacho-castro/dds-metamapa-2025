package ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.FuenteDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.FuenteDTOOutput;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FuenteMapper {

  // ENTITY → DTO
  public static FuenteDTOOutput fuenteToDTO(FuenteAlt fuente) {
    if (fuente == null) return null;

    FuenteDTOOutput dto = new FuenteDTOOutput();
    dto.setId(fuente.getId());
    dto.setNombre(fuente.getNombre());
    dto.setTipoFuente(fuente.getTipo().name()); // enum → String
    dto.setPath(fuente.getPath());
    dto.setPathInfo(fuente.getPathInfo());
    dto.setCantHechos(fuente.getCantHechos());
    return dto;
  }

  // LIST ENTITY → LIST DTO
  public static List<FuenteDTOOutput> fuenteToDTO(List<FuenteAlt> fuentes) {
    if (fuentes == null) return Collections.emptyList();

    return fuentes.stream()
        .map(FuenteMapper::fuenteToDTO)
        .collect(Collectors.toList());
  }

  // DTO → ENTITY
  public static FuenteAlt dtoToFuente(FuenteDTOInput dto) {
    if (dto == null) return null;

    FuenteAlt fuente = new FuenteAlt();
    fuente.setNombre(dto.getNombre());
    fuente.setPath(dto.getPath());
    fuente.setPathInfo(dto.getPathInfo());

    try {
      fuente.setTipo(TipoFuente.valueOf(dto.getTipoFuente().toUpperCase()));
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Tipo de fuente inválido: " + dto.getTipoFuente());
    }

    return fuente;
  }

  // LIST DTO → LIST ENTITY
  public static List<FuenteAlt> dtoToFuente(List<FuenteDTOInput> inputs) {
    if (inputs == null) return Collections.emptyList();

    return inputs.stream()
        .map(FuenteMapper::dtoToFuente)
        .collect(Collectors.toList());
  }
}
