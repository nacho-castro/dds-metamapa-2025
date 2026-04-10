package ar.utn.ba.ddsi.fuenteProxy.models.dtos.input.external.metamapa;

import lombok.Data;

import java.util.List;

@Data
public class MetaMapaResponseHechosDTO {
  private List<MetaMapaHechoDTO> hechos;
}
