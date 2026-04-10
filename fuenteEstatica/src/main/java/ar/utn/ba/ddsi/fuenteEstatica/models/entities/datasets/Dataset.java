package ar.utn.ba.ddsi.fuenteEstatica.models.entities.datasets;

import ar.utn.ba.ddsi.fuenteEstatica.models.entities.hechos.Hecho;

import java.util.List;

public interface Dataset {
  public List<Hecho> obtenerInformacion();
}
