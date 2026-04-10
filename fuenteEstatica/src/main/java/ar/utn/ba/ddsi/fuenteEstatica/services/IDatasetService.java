package ar.utn.ba.ddsi.fuenteEstatica.services;

import ar.utn.ba.ddsi.fuenteEstatica.models.entities.datasets.Dataset;
import ar.utn.ba.ddsi.fuenteEstatica.models.entities.datasets.DatasetType;
import ar.utn.ba.ddsi.fuenteEstatica.models.entities.hechos.Hecho;

import java.util.List;

public interface IDatasetService {
  public List<Hecho> obtenerInformacion(Dataset dataset);
  Dataset construirDataset(DatasetType tipo, String path); //PATRON FACTORY METHOD
}
