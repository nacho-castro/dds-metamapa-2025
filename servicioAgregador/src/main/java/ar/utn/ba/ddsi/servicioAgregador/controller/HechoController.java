package ar.utn.ba.ddsi.servicioAgregador.controller;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.HechoDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.HechoDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.PaginacionResponseDTO;
import ar.utn.ba.ddsi.servicioAgregador.services.IHechoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/hechos")
public class HechoController {
  @Autowired
  private IHechoService hechoService;

  // GET PAGINADO + FILTROS OPCIONALES
  @GetMapping
  public ResponseEntity<PaginacionResponseDTO<HechoDTOOutput>> getHechos(
      @RequestParam(name="page", defaultValue = "0") int page,
      @RequestParam(name="limit", defaultValue = "12") int limit,
      @RequestParam(name="titulo", required = false) String titulo,
      @RequestParam(name="descripcion", required = false) String descripcion,
      @RequestParam(name="fechaDesde", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesde,
      @RequestParam(name="fechaHasta", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHasta
  ) {

    PaginacionResponseDTO<HechoDTOOutput> hechos = hechoService.obtenerHechos(
        page, limit, titulo, descripcion, fechaDesde, fechaHasta
    );

    if (hechos.getContent().isEmpty()) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(hechos);
  }

  // GET /ID
  @GetMapping("/{id}")
  public ResponseEntity<HechoDTOOutput> getHechoById(@PathVariable("id") Long id) {
    HechoDTOOutput dto = hechoService.obtenerHechoPorId(id);

    if (dto == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(dto);
  }

  // DELETE LOGICO
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteHecho(@PathVariable("id") Long id) {
    boolean eliminado = hechoService.desactivarHecho(id);

    if (!eliminado) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<HechoDTOOutput> updateHecho(
      @PathVariable("id") Long id,
      @RequestBody HechoDTOInput hechoDTO) { // Asegúrate de usar @RequestBody
    try {
      HechoDTOOutput actualizado = hechoService.actualizarHecho(id, hechoDTO);
      return ResponseEntity.ok(actualizado);
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
  }
}
