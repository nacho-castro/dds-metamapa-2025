package ar.utn.ba.ddsi.servicioAgregador.models.entities.algoritmosConsenso;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.FuenteAlt;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;

import java.util.List;

public class MayoriaSimple implements IAlgoritmosConsenso{

    List<FuenteAlt> fuentes;

    public MayoriaSimple(List<FuenteAlt> fuentes) {
        this.fuentes = fuentes;
    }

    // si al menos la mitad de las fuentes contienen el mismo hecho,
    // se lo considera consensuado;

    @Override
    public Boolean estaConsensuado(Hecho hecho, List<Hecho> hechos) {
        long apariciones = fuentes.stream()
            .filter(f -> hecho.perteneceA(f))
            .count();

        long necesarias = (long) Math.ceil(fuentes.size() / 2.0);

        return apariciones >= necesarias;
    }
}