package ar.utn.ba.ddsi.servicioAgregador.controller;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.FuenteDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.FuenteDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.services.IFuenteService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fuentes")
public class FuenteController {
  private final IFuenteService fuenteService;

  public FuenteController(IFuenteService fuenteService) {
    this.fuenteService = fuenteService;
  }

  // --- Operaciones CRUD sobre las fuentes (admin) ---

  //CREATE
  @PostMapping
  public ResponseEntity<FuenteDTOOutput> crearFuente(@RequestBody FuenteDTOInput fuenteInput) {
    FuenteDTOOutput fuente = fuenteService.guardarFuente(fuenteInput);
    return new ResponseEntity<>(fuente, HttpStatus.CREATED);
  }

  //READ
  @GetMapping
  public ResponseEntity<List<FuenteDTOOutput>> obtenerFuentes() {
    List<FuenteDTOOutput> fuentes = fuenteService.obtenerFuentes();
    if (fuentes.isEmpty()) {
      return ResponseEntity.noContent().build(); // 204
    }
    return ResponseEntity.ok(fuentes); // 200
  }

  @GetMapping("/{id}")
  public ResponseEntity<FuenteDTOOutput> getFuenteById(
      @PathVariable("id") Long id) {
    FuenteDTOOutput fuente = fuenteService.buscarFuente(id);
    if (fuente == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(fuente);
  }

  //UPDATE (admin)
  @PutMapping("/{id}")
  public ResponseEntity<FuenteDTOOutput> updateFuente(
      @PathVariable("id") Long id,
      @RequestBody FuenteDTOInput dto) {

    FuenteDTOOutput actualizada = fuenteService.actualizarFuente(id, dto);
    return ResponseEntity.ok(actualizada);
  }

  // DELETE (admin)
  @DeleteMapping("/{id}")
  public ResponseEntity<FuenteDTOOutput> deleteFuente(
      @PathVariable("id") Long id) {
    FuenteDTOOutput borrada = fuenteService.borrarFuente(id);
    return ResponseEntity.ok(borrada);
  }

}
