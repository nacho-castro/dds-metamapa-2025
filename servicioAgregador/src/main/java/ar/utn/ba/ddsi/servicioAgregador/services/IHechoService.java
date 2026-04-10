package ar.utn.ba.ddsi.servicioAgregador.services;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.HechoDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.HechoDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.PaginacionResponseDTO;

import java.time.LocalDateTime;

public interface IHechoService {
  public PaginacionResponseDTO<HechoDTOOutput> obtenerHechos(int page, int limit, String titulo, String descripcion, LocalDateTime fechaDesde, LocalDateTime fechaHasta);
  public HechoDTOOutput obtenerHechoPorId(Long id);
  public boolean desactivarHecho(Long id);
  HechoDTOOutput actualizarHecho(Long id, HechoDTOInput dto);
}
