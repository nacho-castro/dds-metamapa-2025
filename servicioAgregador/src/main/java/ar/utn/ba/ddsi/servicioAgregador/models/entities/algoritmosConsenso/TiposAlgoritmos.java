package ar.utn.ba.ddsi.servicioAgregador.models.entities.algoritmosConsenso;

public enum TiposAlgoritmos {
    ABSOLUTA,
    MULTIPLESMENCIONES,
    MAYORIASIMPLE,
    NOHAYALGORITMO
}

//PATRON FACTORY METHOD:
//Recibe un parámetro (el tipo de algoritmo)
//Crea una subclase concreta (del tipo IAlgoritmo)
//Oculta la lógica de construcción al cliente