package ar.utn.ba.ddsi.servicioAgregador.services;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.ColeccionDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.EditColeccionDTO;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.ColeccionDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.HechoDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.PaginacionResponseDTO;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.algoritmosConsenso.TiposAlgoritmos;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.FuenteAlt;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface IColeccionService {
  //CRUD COLECCIONES
  public ColeccionDTOOutput guardarColeccion(ColeccionDTOInput coleccion);
  public PaginacionResponseDTO<ColeccionDTOOutput> obtenerColecciones(int page, int limit, String titulo);
  public ColeccionDTOOutput buscarColeccion(Long idColeccion);
  public ColeccionDTOOutput actualizarColeccion(Long idColeccion, EditColeccionDTO nueva);
  public ColeccionDTOOutput borrarColeccion(Long idColeccion);
  public void modificarAlgoritmoConsenso(Long idColeccion, TiposAlgoritmos algoritmoConsenso);
  public void agregarFuenteAColeccion(Long idColeccion, FuenteAlt fuenteAlt);
  public void quitarFuenteDeColeccion(Long idColeccion, FuenteAlt fuenteAlt);

  public void refrescarColecciones() throws IOException,InterruptedException;

  //OBTENER HECHOS DE LA COLECCION
  public List<HechoDTOOutput> obtenerHechos(Long idColeccion, boolean curada);
  public PaginacionResponseDTO<HechoDTOOutput> obtenerHechosPaginados(
      int page, int limit, Long idColeccion, boolean curada,
      String keyword, String categoria, LocalDate fechaInicio, LocalDate fechaFin);
}
