package ar.utn.ba.ddsi.servicioAgregador.models.entities.algoritmosConsenso;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.FuenteAlt;

import java.util.List;

public class AlgoritmoFactory {
  public static IAlgoritmosConsenso crearAlgoritmo(TiposAlgoritmos tipo, List<FuenteAlt> fuentesSistema) {
    return switch (tipo) {
      case ABSOLUTA -> new Absoluta(fuentesSistema);
      case MULTIPLESMENCIONES -> new MultiplesMenciones(fuentesSistema);
      case MAYORIASIMPLE -> new MayoriaSimple(fuentesSistema);
      case NOHAYALGORITMO -> new NoHayAlgoritmo();
    };
  }
}

//PATRON FACTORY METHOD:
//Recibe un parámetro (el tipo de algoritmo)
//Crea una subclase concreta (del tipo IAlgoritmo)
//Oculta la lógica de construcción al cliente