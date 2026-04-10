package ar.utn.ba.ddsi.fuenteEstatica.controller;

import ar.utn.ba.ddsi.fuenteEstatica.models.dtos.output.HechoDTOOutput;
import ar.utn.ba.ddsi.fuenteEstatica.models.entities.datasets.DatasetType;
import ar.utn.ba.ddsi.fuenteEstatica.services.IHechoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/estatica")
public class FuenteEstaticaController {
  private IHechoService hechoService;

  public FuenteEstaticaController(IHechoService iHechoService) {
    this.hechoService = iHechoService;
  }

  //GET LISTA DE HECHOS DE UN DATASET
  @GetMapping("/{tipo}/{path}")
  public List<HechoDTOOutput> obtenerHechos(
      @PathVariable("tipo") DatasetType tipo,
      @PathVariable("path") String path) {
    return hechoService.obtenerHechosDesde(tipo, path);
  }

  @GetMapping("/{tipo}/{path}/{titulo}")
  public HechoDTOOutput obtenerHechoPorTitulo(
      @PathVariable("tipo") DatasetType tipo,
      @PathVariable("path") String path,
      @PathVariable("titulo") String titulo) {
    return hechoService.getHechoByTitulo(titulo, tipo, path);
  }

}
