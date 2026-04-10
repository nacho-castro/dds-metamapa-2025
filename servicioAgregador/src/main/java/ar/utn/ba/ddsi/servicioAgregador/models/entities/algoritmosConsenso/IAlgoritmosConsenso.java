package ar.utn.ba.ddsi.servicioAgregador.models.entities.algoritmosConsenso;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.Coleccion;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;

import java.util.List;

public interface IAlgoritmosConsenso {
    public Boolean estaConsensuado(Hecho hecho, List<Hecho> hechos);
}

//MOTIVO: comparten todos el mismo metodo estaConsensuado()
//pero se comportan diferente (Polimorfismo)

// List<Hecho> se pasa porque algunos algoritmos,
// como “múltiples menciones”,
// necesitan comparar entre hechos con el mismo título.