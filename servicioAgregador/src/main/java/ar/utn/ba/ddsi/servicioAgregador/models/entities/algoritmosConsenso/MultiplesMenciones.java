package ar.utn.ba.ddsi.servicioAgregador.models.entities.algoritmosConsenso;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.FuenteAlt;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;
import java.util.List;

public class MultiplesMenciones implements IAlgoritmosConsenso{
    List<FuenteAlt> fuentes;

    public MultiplesMenciones(List<FuenteAlt> fuentes) {
        this.fuentes = fuentes;
    }

    //si al menos dos fuentes contienen un mismo hecho
    // y ninguna otra fuente contiene otro de igual título pero diferentes atributos,
    // se lo considera consensuado;

    @Override
    public Boolean estaConsensuado(Hecho hecho, List<Hecho> hechos) {
        // 1. Al menos dos fuentes contienen este hecho
        long apariciones = fuentes.stream()
            .filter(f -> hecho.perteneceA(f))
            .count();

        if (apariciones < 2) return false;

        // 2. No puede haber otro hecho "rival" con mismo título y atributos distintos
        List<Hecho> mismosTitulo = hechos.stream()
            .filter(h -> h.getTitulo().equalsIgnoreCase(hecho.getTitulo()))
            .toList();

        // si existe otro distinto -> NO consensuado
        boolean hayConflictos = mismosTitulo.stream()
            .anyMatch(h -> !h.equals(hecho)); // importante equals basado en atributos

        return !hayConflictos;
    }
}

