package ar.utn.ba.ddsi.fuenteProxy.models.entities.hechos;

import ar.utn.ba.ddsi.fuenteProxy.models.dtos.output.EtiquetaDTOOutput;
import ar.utn.ba.ddsi.fuenteProxy.models.dtos.output.HechoDTOOutput;
import ar.utn.ba.ddsi.fuenteProxy.models.dtos.output.LugarDTOOutput;

import java.util.Collections;
import java.util.List;

public class HechoMapper {
  //CLASE UTILITARIA ENCARGADA DE MAPEAR HECHOS A DTOs

  public static HechoDTOOutput hechoToDTO(Hecho hecho) {
    HechoDTOOutput dto = new HechoDTOOutput();
    dto.setTitulo(hecho.getTitulo());
    dto.setDescripcion(hecho.getDescripcion());
    dto.setCategoria(hecho.getCategoria());
    dto.setFechaAcontecimiento(hecho.getFechaAcontecimiento());
    dto.setLugarAcontecimiento(lugarToDTO(hecho.getLugarAcontecimiento()));
    dto.setEtiquetas(etiquetasToDTOs(hecho.getEtiquetas()));
    return dto;
  }

  public static LugarDTOOutput lugarToDTO(Lugar lugar) {
    LugarDTOOutput dto = new LugarDTOOutput();
    dto.setLatitud(lugar.getLatitud());
    dto.setLongitud(lugar.getLongitud());
    return dto;
  }

  public static List<EtiquetaDTOOutput> etiquetasToDTOs(List<Etiqueta> etiquetas) {
    if (etiquetas == null) {
      return Collections.emptyList();
    }
    return etiquetas.stream().map(e -> {
      EtiquetaDTOOutput dto = new EtiquetaDTOOutput();
      dto.setNombre(e.getNombre());
      return dto;
    }).toList();
  }
}
