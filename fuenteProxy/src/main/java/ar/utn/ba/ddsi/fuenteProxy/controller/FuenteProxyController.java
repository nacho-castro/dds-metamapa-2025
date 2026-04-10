package ar.utn.ba.ddsi.fuenteProxy.controller;

import ar.utn.ba.ddsi.fuenteProxy.models.dtos.output.HechoDTOOutput;
import ar.utn.ba.ddsi.fuenteProxy.services.IProxyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/proxy")
public class FuenteProxyController {

  //Seleccionar dinámicamente cuál api llamar
  private IProxyService proxyService;

  public FuenteProxyController(IProxyService proxyService){
    this.proxyService = proxyService;
  }

  //Pasar el nombre de la API Ej. "metamapa" o "disilab"
  @GetMapping("/{api}")
  public List<HechoDTOOutput> buscarTodos(@PathVariable("api") String api){
    return this.proxyService.getHechos(api);
  }

  @GetMapping("/{api}/{id}")
  public HechoDTOOutput buscarHechoPorId(@PathVariable("api") String api, @PathVariable("id") Long id){
    return this.proxyService.getHechoById(api, id);
  }
}
