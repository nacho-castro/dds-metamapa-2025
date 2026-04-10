package ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos;


import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.HechoDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.HechoDinamicoDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.LugarDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.HechoDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.LugarDTOOutput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HechoMapper {
  //CLASE UTILITARIA ENCARGADA DE MAPEAR HECHOS A DTOs
  public static Hecho DTOToHecho(HechoDTOInput hechoInput) {
    Hecho hecho = new Hecho();
    hecho.setTitulo(hechoInput.getTitulo());
    hecho.setDescripcion(hechoInput.getDescripcion());
    hecho.setCategoria(hechoInput.getCategoria());
    hecho.setFechaAcontecimiento(hechoInput.getFechaAcontecimiento());
    hecho.setLugarAcontecimiento(DTOToLugar(hechoInput.getLugarAcontecimiento()));
    hecho.setUsuarioId(hechoInput.getUsuarioid());
    return hecho;
  }

  public static List<Hecho> DTOToHecho(List<HechoDTOInput> dtos){
    if (dtos == null) return List.of();

    return dtos.stream()
        .map(HechoMapper::DTOToHecho) //método anterior
        .collect(Collectors.toList());
  }

  public static Lugar DTOToLugar(LugarDTOInput lugarInput) {
    if (lugarInput == null) return null;
    return new Lugar(lugarInput.getLatitud(),lugarInput.getLongitud());
  }

  public static HechoDTOOutput hechoToDTO(Hecho hecho) {
    HechoDTOOutput dto = new HechoDTOOutput();
    dto.setId(hecho.getId());
    dto.setTitulo(hecho.getTitulo());
    dto.setDescripcion(hecho.getDescripcion());
    dto.setCategoria(hecho.getCategoria());
    dto.setFechaAcontecimiento(hecho.getFechaAcontecimiento());
    dto.setFechaCarga(hecho.getFechaCarga());
    dto.setLugarAcontecimiento(lugarToDTO(hecho.getLugarAcontecimiento()));
    dto.setActivo(hecho.getActivo());
    dto.setFuentes(hecho.getFuenteDeOrigen()
            .stream()
            .map(f -> f.getId())
            .toList()
    );
    dto.setUsuarioId(
        hecho.getUsuarioId() != null ? hecho.getUsuarioId() : null
    );
    return dto;
  }

  public static List<HechoDTOOutput> hechoToDTO(List<Hecho> hechos){
    if (hechos == null) return List.of();

    return hechos.stream()
        .map(HechoMapper::hechoToDTO) //método anterior
        .collect(Collectors.toList());
  }

  public static LugarDTOOutput lugarToDTO(Lugar lugar) {
    LugarDTOOutput dto = new LugarDTOOutput();
    dto.setLatitud(lugar.getLatitud());
    dto.setLongitud(lugar.getLongitud());
    dto.setProvincia(lugar.getProvincia());
    return dto;
  }

  public static Hecho DTODinToHecho(HechoDinamicoDTOInput hechoInput) {
    Hecho hecho = new Hecho();
    hecho.setTitulo(hechoInput.getTitulo());
    hecho.setDescripcion(hechoInput.getDescripcion());
    hecho.setCategoria(hechoInput.getCategoria());
    hecho.setFechaAcontecimiento(hechoInput.getFechaAcontecimiento());
    hecho.setLugarAcontecimiento(DTOToLugar(hechoInput.getLugarAcontecimiento()));
    return hecho;
  }

}
