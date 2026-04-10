package ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos;

import ar.utn.ba.ddsi.fuenteDinamica.models.dtos.input.HechoDTOInput;
import ar.utn.ba.ddsi.fuenteDinamica.models.dtos.output.HechoDTOOutput;
import ar.utn.ba.ddsi.fuenteDinamica.models.dtos.output.LugarDTOOutput;

public class HechoMapper {
  //CLASE UTILITARIA ENCARGADA DE MAPEAR HECHOS A DTOs
  public static Hecho DTOToHecho(HechoDTOInput hechoInput) {
    Hecho hecho = new Hecho();
    hecho.setTitulo(hechoInput.getTitulo());
    hecho.setDescripcion(hechoInput.getDescripcion());
    hecho.setCategoria(hechoInput.getCategoria());
    hecho.setFechaAcontecimiento(hechoInput.getFechaAcontecimiento());
    hecho.setLugarAcontecimiento(DTOToLugar(hechoInput.getLugarAcontecimiento()));
    hecho.setEtiquetas(hechoInput.getEtiquetas());
    hecho.setMultimediaUrls(hechoInput.getMultimediaUrls());
    return hecho;
  }

  public static Lugar DTOToLugar(LugarDTOOutput lugarInput) {
    if (lugarInput == null) return null;
    return new Lugar(lugarInput.getLatitud(),lugarInput.getLongitud());
  }

  public static HechoDTOOutput hechoToDTO(Hecho hecho) {
    HechoDTOOutput dto = new HechoDTOOutput();
    dto.setTitulo(hecho.getTitulo());
    dto.setDescripcion(hecho.getDescripcion());
    dto.setCategoria(hecho.getCategoria());
    dto.setFechaAcontecimiento(hecho.getFechaAcontecimiento());
    dto.setFechaCarga(hecho.getFechaCarga());
    dto.setLugarAcontecimiento(lugarToDTO(hecho.getLugarAcontecimiento()));
    dto.setEtiquetas(hecho.getEtiquetas());
    dto.setCreador(hecho.getUsuarioId());
    dto.setEditable(hecho.isEditable());
    return dto;
  }

  public static LugarDTOOutput lugarToDTO(Lugar lugar) {
    LugarDTOOutput dto = new LugarDTOOutput();
    dto.setLatitud(lugar.getLatitud());
    dto.setLongitud(lugar.getLongitud());
    return dto;
  }

}
