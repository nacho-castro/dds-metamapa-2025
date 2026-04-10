package ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.ColeccionDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.FuenteDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.ColeccionDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.FuenteAlt;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.TipoFuente;

import java.util.Collections;
import java.util.List;

public class ColeccionMapper {
  //CLASE UTILITARIA ENCARGADA DE MAPEAR HECHOS A DTOs

  public static ColeccionDTOOutput coleccionToDTO(Coleccion coleccion) {
    ColeccionDTOOutput dto = new ColeccionDTOOutput();
    dto.setId(coleccion.getId());
    dto.setTitulo(coleccion.getTitulo());
    dto.setDescripcion(coleccion.getDescripcion());
    dto.setAlgoritmoConsenso(coleccion.getAlgoritmoConsenso());
    return dto;
  }

  //LISTA A DTO
  public static List<ColeccionDTOOutput> coleccionToDTO(List<Coleccion> colecciones) {
    if (colecciones == null) return Collections.emptyList();

    return colecciones.stream()
        .map(ColeccionMapper::coleccionToDTO)
        .toList();
  }

  public static Coleccion dtoToColeccion(ColeccionDTOInput input) {
    Coleccion coleccion = new Coleccion();
    coleccion.setTitulo(input.getTitulo());
    coleccion.setDescripcion(input.getDescripcion());
    coleccion.setAlgoritmoConsenso(input.getAlgoritmoConsenso());

    return coleccion;
  }

  public static List<Coleccion> dtoToColeccion(List<ColeccionDTOInput> inputs) {
    if (inputs == null) return Collections.emptyList();

    return inputs.stream()
        .map(ColeccionMapper::dtoToColeccion)
        .toList();
  }
}
