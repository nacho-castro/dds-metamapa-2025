package ar.edu.utn.frba.dds.tpa.estadisticas.client;

import ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.ColeccionDTO;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.HechoDTO;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.SolicitudDTO;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.externalDTO.ColeccionDTOInput;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.externalDTO.HechoDTOInput;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.externalDTO.SolicitudEliminacionInput;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.entities.Estado;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AgregadorMapper {

  public HechoDTO mapToHechoDTO(HechoDTOInput input) {
    return new HechoDTO(
        input.getId(),
        input.getCategoria(),
        input.getLugarAcontecimiento() != null ? input.getLugarAcontecimiento().getProvincia() : null,
        input.getFechaAcontecimiento()
    );
  }

  public List<HechoDTO> mapToHechoDTO(List<HechoDTOInput> inputs) {
    return inputs.stream()
        .map(this::mapToHechoDTO)
        .toList();
  }

  public ColeccionDTO mapToColeccionDTO(ColeccionDTOInput input, List<HechoDTOInput> hechosInput) {
    ColeccionDTO dto = new ColeccionDTO();
    dto.setId(input.getId());
    dto.setTitulo(input.getTitulo());
    // Mapear la lista de hechos
    if (hechosInput != null) {
      dto.setHechos(hechosInput.stream()
          .map(this::mapToHechoDTO)
          .toList());
    }
    return dto;
  }

  public SolicitudDTO mapToSolicitudDTO(SolicitudEliminacionInput input) {
    SolicitudDTO dto = new SolicitudDTO(input.getId());
    // Convertimos estado de String a Enum si es posible
    try {
      dto.setEstado(Estado.valueOf(input.getEstado()));
    } catch (Exception e) {
      dto.setEstado(Estado.PENDIENTE); // o algún valor por defecto
    }
    return dto;
  }
}
