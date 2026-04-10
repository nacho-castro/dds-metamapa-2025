package ar.utn.ba.ddsi.servicioAgregador.models.entities.algoritmosConsenso;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.FuenteAlt;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;

import java.util.List;

public class Absoluta implements IAlgoritmosConsenso {
  List<FuenteAlt> fuentes;

  public Absoluta(List<FuenteAlt> fuentes) {
    this.fuentes = fuentes;
  }

  //si todas las fuentes contienen el mismo, se lo considera consensuado.

  @Override
  public Boolean estaConsensuado(Hecho hecho, List<Hecho> hechos) {
    return fuentes.stream()
        .allMatch(f -> hecho.perteneceA(f)); // usa fuente_x_hecho
  }
}