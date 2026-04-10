package ar.utn.ba.ddsi.servicioAgregador.controller;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.SolicitudEdicionDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.SolicitudEliminacionInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.PaginacionResponseDTO;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.SolicitudEdicionDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.SolicitudEliminacionDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.services.ISolicitudEdicionService;
import ar.utn.ba.ddsi.servicioAgregador.services.ISolicitudEliminacionService;
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

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {
  private final ISolicitudEliminacionService solEliminicacionService;
  private final ISolicitudEdicionService solEdicionService;

  public SolicitudController(ISolicitudEliminacionService solEliminicacionService, ISolicitudEdicionService solEdicionService) {
    this.solEliminicacionService = solEliminicacionService;
    this.solEdicionService = solEdicionService;
  }

  //OBTENER SOLICITUDES DE ELIMINACION
  @GetMapping("/eliminacion")
  public ResponseEntity<PaginacionResponseDTO<SolicitudEliminacionDTOOutput>> getSolicitudes(
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "limit", required = false, defaultValue = "10") int limit)
  {
    PaginacionResponseDTO<SolicitudEliminacionDTOOutput> solicitudes = solEliminicacionService.buscarPaginado(page,limit);
    return ResponseEntity.ok(solicitudes);
  }

  //CREAR SOLICITUD DE ELIMINACION (contribuyente)
  @PostMapping("/eliminacion")
  public ResponseEntity<SolicitudEliminacionDTOOutput> generarSolicitud(@RequestBody SolicitudEliminacionInput dto) {
    SolicitudEliminacionDTOOutput solicitudCreada = solEliminicacionService.crearSolicitud(dto);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(solicitudCreada);
  }

  // --- Aprobar o denegar una solicitud de eliminación de un hecho ---

  //APROBAR (admin)
  @PatchMapping("/eliminacion/{solicitudId}")
  public ResponseEntity<Void> aprobarSolicitudEliminacion(
      @PathVariable("solicitudId") Long solicitudId) {
    solEliminicacionService.aprobarSolicitudEliminacion(solicitudId);
    return ResponseEntity.noContent().build();
  }

  //DENEGAR (admin)
  @DeleteMapping("/eliminacion/{solicitudId}")
  public ResponseEntity<Void> denegarSolicitudEliminacion(
      @PathVariable("solicitudId") Long solicitudId) {
    solEliminicacionService.denegarSolicitudEliminacion(solicitudId);
    return ResponseEntity.noContent().build();
  }

  // 1. OBTENER SOLICITUDES DE EDICIÓN PAGINADAS
  @GetMapping("/edicion")
  public ResponseEntity<PaginacionResponseDTO<SolicitudEdicionDTOOutput>> getSolicitudesEdicion(
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {

    // Nota: Deberás actualizar tu servicio para soportar paginación o usar buscarTodos temporalmente
    PaginacionResponseDTO<SolicitudEdicionDTOOutput> response = solEdicionService.buscarPaginado(page, limit);
    return ResponseEntity.ok(response);
  }

  // 2. CREAR SOLICITUD DE EDICIÓN (Contribuyente)
  @PostMapping("/edicion")
  public ResponseEntity<SolicitudEdicionDTOOutput> crearSolicitudEdicion(@RequestBody SolicitudEdicionDTOInput dto) {
    SolicitudEdicionDTOOutput creada = solEdicionService.crearSolicitud(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(creada);
  }

  // 3. APROBAR EDICIÓN (Admin)
  @PatchMapping("/edicion/{id}")
  public ResponseEntity<Void> aprobarEdicion(@PathVariable("id") Long id) {
    solEdicionService.aprobarSolicitudEdicion(id);
    return ResponseEntity.noContent().build();
  }

  // 4. DENEGAR EDICIÓN (Admin)
  @DeleteMapping("/edicion/{id}")
  public ResponseEntity<Void> denegarEdicion(@PathVariable("id") Long id) {
    solEdicionService.denegarSolicitudEdicion(id);
    return ResponseEntity.noContent().build();
  }

}
