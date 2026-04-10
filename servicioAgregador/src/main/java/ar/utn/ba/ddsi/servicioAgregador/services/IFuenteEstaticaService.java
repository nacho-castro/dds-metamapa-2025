package ar.utn.ba.ddsi.servicioAgregador.services;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.FuenteAlt;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;

import java.util.List;

public interface IFuenteEstaticaService {
  public List<Hecho> obtenerHechos(FuenteAlt fuente); //BUSCAR A PARTIR DE DATASETS
}
//LA FUENTE ESTATICA SOLO LEE CONJUNTOS DE DATASETS