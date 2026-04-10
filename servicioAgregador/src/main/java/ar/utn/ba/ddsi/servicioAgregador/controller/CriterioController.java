package ar.utn.ba.ddsi.servicioAgregador.controller;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.CriterioDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.criterios.Criterio;
import ar.utn.ba.ddsi.servicioAgregador.services.impl.CriterioService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/criterios")
public class CriterioController {

  private final CriterioService criterioService;

  public CriterioController(CriterioService criterioService) {
    this.criterioService = criterioService;
  }

  @PostMapping
  public ResponseEntity<Criterio> crear(@RequestBody CriterioDTOInput dto) {
    return ResponseEntity.ok(criterioService.crearCriterio(dto));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Criterio> actualizar(
      @PathVariable Long id,
      @RequestBody CriterioDTOInput dto
  ) {
    return ResponseEntity.ok(criterioService.actualizarCriterio(id, dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
    criterioService.eliminarCriterio(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Criterio> obtener(@PathVariable("id") Long id) {
    return ResponseEntity.ok(criterioService.obtener(id));
  }

  @GetMapping
  public ResponseEntity<List<Criterio>> listar() {
    return ResponseEntity.ok(criterioService.listar());
  }
}
