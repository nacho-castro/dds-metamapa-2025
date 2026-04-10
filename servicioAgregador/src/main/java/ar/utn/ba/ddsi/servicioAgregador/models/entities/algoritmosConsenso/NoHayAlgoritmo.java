package ar.utn.ba.ddsi.servicioAgregador.models.entities.algoritmosConsenso;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;

import java.util.List;

public class NoHayAlgoritmo implements IAlgoritmosConsenso{
    @Override
    public Boolean estaConsensuado(Hecho hecho, List<Hecho> hechos) {
        return true;
    }
}
