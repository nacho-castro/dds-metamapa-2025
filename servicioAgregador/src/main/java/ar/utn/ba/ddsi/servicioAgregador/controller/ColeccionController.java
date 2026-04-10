package ar.utn.ba.ddsi.servicioAgregador.controller;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.ColeccionDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.EditColeccionDTO;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.ColeccionDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.HechoDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.PaginacionResponseDTO;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.algoritmosConsenso.TiposAlgoritmos;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.FuenteAlt;
import ar.utn.ba.ddsi.servicioAgregador.services.IColeccionService;
import ar.utn.ba.ddsi.servicioAgregador.services.IFuenteService;
import ar.utn.ba.ddsi.servicioAgregador.services.IHechoService;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/colecciones")
public class ColeccionController {
  private final IColeccionService coleccionService;
  private final IHechoService hechoService;
  private final IFuenteService fuenteService;

  public ColeccionController(IColeccionService coleccionService, IHechoService hechoService, IFuenteService fuenteService) {
    this.coleccionService = coleccionService;
    this.hechoService = hechoService;
    this.fuenteService = fuenteService;
  }

  // --- Operaciones CRUD sobre las colecciones (admin) ---

  //CREATE COLECCION (ADMIN)
  @PostMapping
  public ResponseEntity<ColeccionDTOOutput> crearColeccion(@RequestBody ColeccionDTOInput coleccionDTOInput) {
    System.out.println("=== RECIBIENDO PETICIÓN ===");
    System.out.println("DTO recibido: " + coleccionDTOInput);
    System.out.println("Título: " + coleccionDTOInput.getTitulo());
    System.out.println("Descripción: " + coleccionDTOInput.getDescripcion());
    System.out.println("Algoritmo: " + coleccionDTOInput.getAlgoritmoConsenso());
    System.out.println("Fuentes: " + coleccionDTOInput.getFuentes());
    try {
      ColeccionDTOOutput nuevaColeccion = coleccionService.guardarColeccion(coleccionDTOInput);
      return new ResponseEntity<>(nuevaColeccion, HttpStatus.CREATED);
    } catch (Exception e) {
      System.err.println("=== ERROR AL GUARDAR ===");
      System.err.println("Mensaje: " + e.getMessage());
      throw e;
    }
  }

  //GET COLECCIONES (con paginacion)
  @GetMapping
  public ResponseEntity<PaginacionResponseDTO<ColeccionDTOOutput>> getColecciones(
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "limit", required = false, defaultValue = "9") int limit,
      @RequestParam(name = "titulo", required = false) String titulo) {

    PaginacionResponseDTO<ColeccionDTOOutput> colecciones = coleccionService.obtenerColecciones(page, limit, titulo);

    if (colecciones.getContent().isEmpty()) {
      return ResponseEntity.noContent().build(); // 204
    }
    return ResponseEntity.ok(colecciones); // 200
  }

  @GetMapping("/{id}")
  public ResponseEntity<ColeccionDTOOutput> getColeccionById(@PathVariable("id") Long id) {
    ColeccionDTOOutput coleccion = coleccionService.buscarColeccion(id);
    if (coleccion == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(coleccion);
  }

  //UPDATE COLECCION (Admin)
  @PutMapping("/{id}")
  public ResponseEntity<ColeccionDTOOutput> updateColeccion(
      @PathVariable("id") Long id,
      @RequestBody EditColeccionDTO dto) {

    ColeccionDTOOutput actualizada = coleccionService.actualizarColeccion(id, dto);
    return ResponseEntity.ok(actualizada);
  }

  // DELETE COLECCION (admin)
  @DeleteMapping("/{id}")
  public ResponseEntity<ColeccionDTOOutput> deleteColeccion(@PathVariable("id") Long id) {
    ColeccionDTOOutput borrada = coleccionService.borrarColeccion(id);
    return ResponseEntity.ok(borrada);
  }

  // --- Obtener HECHOS de una colección con modo de navegación ---
  @GetMapping("/{id}/hechos")
  public ResponseEntity<PaginacionResponseDTO<HechoDTOOutput>> getHechosDeColeccion(
      @PathVariable("id") Long id,
      @RequestParam(name = "curada", required = false, defaultValue = "false") boolean curada,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "limit", required = false, defaultValue = "12") int limit,
      // --- NUEVOS PARÁMETROS ---
      @RequestParam(name = "keyword", required = false) String keyword,
      @RequestParam(name = "categoria", required = false) String categoria,
      @RequestParam(name = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
      @RequestParam(name = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
  ) {

    // Pasamos todos los parámetros al servicio
    PaginacionResponseDTO<HechoDTOOutput> hechosOutput = coleccionService.obtenerHechosPaginados(
        page, limit, id, curada, keyword, categoria, fechaInicio, fechaFin
    );

    return new ResponseEntity<>(hechosOutput, HttpStatus.OK);
  }

  // --- Modificación del algoritmo de consenso (ADMIN)---
  @PatchMapping("/{id}/algoritmo-consenso")
  public ResponseEntity<Void> modificarAlgoritmoConsenso(
      @PathVariable("id") Long id,
      @RequestParam("algoritmo") TiposAlgoritmos algoritmo) {
    coleccionService.modificarAlgoritmoConsenso(id, algoritmo);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  // --- Agregar o quitar fuentes de hechos de una colección (ADMIN)---

  @PostMapping("/{id}/fuentes/{fuenteId}")
  public ResponseEntity<Void> agregarFuenteAColeccion(
      @PathVariable("id") Long idColeccion,
      @PathVariable("fuenteId") Long fuenteId) {
    FuenteAlt fuente = fuenteService.encontrarFuente(fuenteId);
    coleccionService.agregarFuenteAColeccion(idColeccion, fuente);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}/fuentes/{fuenteId}")
  public ResponseEntity<Void> quitarFuenteDeColeccion(
      @PathVariable("id") Long idColeccion,
      @PathVariable("fuenteId") Long fuenteId) {
    FuenteAlt fuente = fuenteService.encontrarFuente(fuenteId);
    coleccionService.quitarFuenteDeColeccion(idColeccion, fuente);
    return ResponseEntity.noContent().build();
  }

  // Endpoint para refrescar manualmente
  @PostMapping("/refrescar")
  public ResponseEntity<String> refrescarColecciones(@RequestBody(required = false) String body) {
    try {
      coleccionService.refrescarColecciones();
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
      return ResponseEntity.ok("Refresco finalizado correctamente");
  }
}
