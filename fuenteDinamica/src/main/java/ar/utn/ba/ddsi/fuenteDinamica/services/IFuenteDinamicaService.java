package ar.utn.ba.ddsi.fuenteDinamica.services;

import ar.utn.ba.ddsi.fuenteDinamica.models.dtos.input.HechoDTOInput;
import ar.utn.ba.ddsi.fuenteDinamica.models.dtos.output.HechoDTOOutput;
import java.util.List;

public interface IFuenteDinamicaService {
  public HechoDTOOutput subirHecho(HechoDTOInput hecho, Long idUsuario);
  public List<HechoDTOOutput> obtenerHechos();
  public HechoDTOOutput buscarHechoPorId(Long id);
  public HechoDTOOutput editarHecho(Long id, HechoDTOInput hechoNuevo, Long idUsuario);
  public HechoDTOOutput eliminarHecho(Long id);
}
