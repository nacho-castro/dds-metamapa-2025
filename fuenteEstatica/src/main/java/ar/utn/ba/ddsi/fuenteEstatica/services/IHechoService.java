package ar.utn.ba.ddsi.fuenteEstatica.services;


import ar.utn.ba.ddsi.fuenteEstatica.models.dtos.output.HechoDTOOutput;
import ar.utn.ba.ddsi.fuenteEstatica.models.entities.datasets.DatasetType;
import ar.utn.ba.ddsi.fuenteEstatica.models.entities.hechos.Hecho;

import java.util.List;

public interface IHechoService {
  //Encargado de construir dataset indicado y retornar HECHOS en forma de DTOs
  //PATRON FACTORY METHOD
  public HechoDTOOutput getHechoByTitulo(String titulo, DatasetType tipo, String path);
  public List<HechoDTOOutput> obtenerHechosDesde(DatasetType tipo, String path);
}
