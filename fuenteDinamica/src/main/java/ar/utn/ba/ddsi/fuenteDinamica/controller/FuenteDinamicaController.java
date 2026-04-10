package ar.utn.ba.ddsi.fuenteDinamica.controller;

import ar.utn.ba.ddsi.fuenteDinamica.models.dtos.input.HechoDTOInput;
import ar.utn.ba.ddsi.fuenteDinamica.models.dtos.output.HechoDTOOutput;
import ar.utn.ba.ddsi.fuenteDinamica.services.IFuenteDinamicaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dinamica")
public class FuenteDinamicaController {
  private IFuenteDinamicaService fuenteDinamicaService;

  public FuenteDinamicaController(IFuenteDinamicaService fuenteDinamicaService) {
    this.fuenteDinamicaService = fuenteDinamicaService;
  }

  //CREATE:POST Hecho
  //un solo POST: acepta JWT opcional.
  //si no viene -> el hecho se registra como anónimo.

  @PostMapping
  public ResponseEntity<HechoDTOOutput> crearHecho(
      @RequestBody HechoDTOInput hechoNuevo,
      HttpServletRequest request) {

    Long idUsuario = (Long) request.getAttribute("id");
    // puede ser null/anónimo

    HechoDTOOutput hecho = fuenteDinamicaService.subirHecho(hechoNuevo, idUsuario);

    return ResponseEntity.status(201).body(hecho);
  }

  //READ: GET HECHOS
  @GetMapping()
  public ResponseEntity<List<HechoDTOOutput>> buscarTodos(){
    List<HechoDTOOutput> hechos = fuenteDinamicaService.obtenerHechos();
    return ResponseEntity.ok(hechos);
  }

  //READ: GET BY ID
  @GetMapping("/{id}")
  public ResponseEntity<HechoDTOOutput>  buscarHechoPorId(@PathVariable("id") Long id){
    HechoDTOOutput hecho = fuenteDinamicaService.buscarHechoPorId(id);
    return ResponseEntity.ok(hecho);
  }

  //UPDATE: PUT HECHO BY ID
  @PutMapping("/{id}")
  public ResponseEntity<HechoDTOOutput> editarHecho(
      @RequestBody HechoDTOInput hechoNuevo,
      @PathVariable("id") Long idHecho,
      HttpServletRequest request
  ){

    Long idUsuario = (Long) request.getAttribute("id");

    HechoDTOOutput actualizado =
        fuenteDinamicaService.editarHecho(idHecho, hechoNuevo, idUsuario);

    return ResponseEntity.ok(actualizado);
  }

  //DELETE: DELETE HECHO BY ID
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminarHecho(@PathVariable("id") Long id){
    fuenteDinamicaService.eliminarHecho(id);
    return ResponseEntity.noContent().build(); // 204 No Content
  }

}
