package ar.utn.ba.ddsi.fuenteEstatica.models.entities.datasets;

public enum DatasetType {
  CSV,
  DB,
}

//PATRON FACTORY METHOD:
//Recibe un parámetro (el tipo de dataset)
//Crea una subclase concreta (DatasetCsv, DatasetDb, etc.)
//Oculta la lógica de construcción al cliente

//Selecciona dinámicamente la implementación de una interfaz (Dataset)

/*
Si más adelante necesito que cada tipo de dataset tenga una forma particular de crearse
pasar a un Abstract Factory o usar el patrón Strategy
para encapsular la lógica de carga.!!!!!
 */