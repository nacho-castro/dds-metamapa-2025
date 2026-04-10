package ar.utn.ba.ddsi.fuenteEstatica.services.impl;

import ar.utn.ba.ddsi.fuenteEstatica.models.dtos.output.EtiquetaDTOOutput;
import ar.utn.ba.ddsi.fuenteEstatica.models.dtos.output.HechoDTOOutput;
import ar.utn.ba.ddsi.fuenteEstatica.models.entities.datasets.Dataset;
import ar.utn.ba.ddsi.fuenteEstatica.models.entities.datasets.DatasetType;
import ar.utn.ba.ddsi.fuenteEstatica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteEstatica.models.entities.hechos.HechoMapper;
import ar.utn.ba.ddsi.fuenteEstatica.services.IDatasetService;
import ar.utn.ba.ddsi.fuenteEstatica.services.IHechoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HechoService implements IHechoService {

  private IDatasetService datasetService;

  public HechoService(IDatasetService datasetService, Dataset dataset) {
    this.datasetService = datasetService;
  }

  //GET HECHOS DTOS A PARTIR DE UN DATASET DEFINIDO
  @Override
  public List<HechoDTOOutput> obtenerHechosDesde(DatasetType tipo, String path) {
    Dataset dataset = datasetService.construirDataset(tipo, path);
    return datasetService.obtenerInformacion(dataset)
        .stream()
        .map(HechoMapper::hechoToDTO)
        .toList();
  }

  //GET HECHO PARTICULAR segun TITULO
  @Override
  public HechoDTOOutput getHechoByTitulo(String titulo, DatasetType tipo, String path) {
    Dataset dataset = datasetService.construirDataset(tipo, path);
    List<Hecho> hechos = datasetService.obtenerInformacion(dataset);
    Hecho hechoEncontrado = hechos.stream().filter(h->h.getTitulo().equals(titulo)).findFirst().get();
    return HechoMapper.hechoToDTO(hechoEncontrado);
  }
}

