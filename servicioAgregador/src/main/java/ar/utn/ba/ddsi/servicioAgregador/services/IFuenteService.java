package ar.utn.ba.ddsi.servicioAgregador.services;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.ColeccionDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.EditColeccionDTO;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.FuenteDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.ColeccionDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.FuenteDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.Coleccion;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.FuenteAlt;
import java.util.List;

public interface IFuenteService {
  public FuenteDTOOutput guardarFuente(FuenteDTOInput fuente);
  public List<FuenteDTOOutput> obtenerFuentes();
  public FuenteDTOOutput buscarFuente(Long id);
  public FuenteAlt encontrarFuente(Long id);
  public FuenteDTOOutput actualizarFuente(Long id, FuenteDTOInput nueva);
  public FuenteDTOOutput borrarFuente(Long id);
}
