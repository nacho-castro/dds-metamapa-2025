package ar.utn.ba.ddsi.frontend.controllers;

import ar.utn.ba.ddsi.frontend.dto.output.PageDTOOutput;
import ar.utn.ba.ddsi.frontend.dto.output.SolicitudAdminDTO;
import ar.utn.ba.ddsi.frontend.dto.output.SolicitudEdicionDTOOutput;
import ar.utn.ba.ddsi.frontend.services.SolicitudService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

  private final SolicitudService solicitudService;

  public AdminController(SolicitudService solicitudService) {
    this.solicitudService = solicitudService;
  }

  // PANEL DE ADMIN (Dashboard)
  @GetMapping()
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public String admin(
      Model model,
      // 1. Usamos nombres distintos para cada paginación
      @RequestParam(name = "pageEliminacion", defaultValue = "0") int pageEliminacion,
      @RequestParam(name = "pageEdicion", defaultValue = "0") int pageEdicion,
      @RequestParam(name = "limit", defaultValue = "5") int limit, // Un límite más bajo (ej: 5 o 10) es mejor para dashboards visuales
      @RequestParam(name = "estado", required = false) String estado
  ) {
    // 2. Llamamos al servicio pasando la página específica de cada sección
    PageDTOOutput<SolicitudAdminDTO> solEliminacion =
        solicitudService.obtenerSolicitudesEliminacion(pageEliminacion, limit, estado);

    PageDTOOutput<SolicitudEdicionDTOOutput> solEdicion =
        solicitudService.obtenerSolicitudesEdicion(pageEdicion, limit);

    // 3. Agregamos las listas
    model.addAttribute("solicitudesEliminacion", solEliminacion.getContent());
    model.addAttribute("solicitudesEdicion", solEdicion.getContent());

    // Mantener compatibilidad si tu JS usa "solicitudes" genérico (opcional)
    model.addAttribute("solicitudes", solEliminacion.getContent());

    // 4. Agregamos metadatos de paginación INDEPENDIENTES al modelo
    // Para Eliminación
    model.addAttribute("pageEliminacion", pageEliminacion);
    model.addAttribute("totalPagesEliminacion", solEliminacion.getTotalPages());

    // Para Edición
    model.addAttribute("pageEdicion", pageEdicion);
    model.addAttribute("totalPagesEdicion", solEdicion.getTotalPages());

    // Params globales para mantener filtros
    model.addAttribute("estado", estado);
    model.addAttribute("page", "admin");

    return "admin";
  }

  // --- ACCIONES DE ELIMINACIÓN ---

  @PostMapping("/solicitudes/{idSolicitud}/aprobar")
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public String aceptarSolicitudEliminacion(
      @PathVariable("idSolicitud") Long idSolicitud, // Nombre explícito
      HttpSession session,
      RedirectAttributes redirectAttributes) {
    try {
      String token = (String) session.getAttribute("accessToken");
      if (token == null) return "redirect:/login";

      solicitudService.aprobarSolicitud(idSolicitud, token);

      redirectAttributes.addFlashAttribute("mensaje", "Solicitud de eliminación aprobada.");
      redirectAttributes.addFlashAttribute("tipoMensaje", "success");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
      redirectAttributes.addFlashAttribute("tipoMensaje", "error");
    }
    return "redirect:/admin";
  }

  @PostMapping("/solicitudes/{idSolicitud}/denegar")
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public String denegarSolicitudEliminacion(
      @PathVariable("idSolicitud") Long idSolicitud, // Nombre explícito
      HttpSession session,
      RedirectAttributes redirectAttributes) {
    try {
      String token = (String) session.getAttribute("accessToken");
      if (token == null) return "redirect:/login";

      solicitudService.denegarSolicitud(idSolicitud, token);

      redirectAttributes.addFlashAttribute("mensaje", "Solicitud de eliminación rechazada.");
      redirectAttributes.addFlashAttribute("tipoMensaje", "info");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
      redirectAttributes.addFlashAttribute("tipoMensaje", "error");
    }
    return "redirect:/admin";
  }

  // --- ACCIONES DE EDICIÓN (NUEVAS) ---

  @PostMapping("/solicitudes/edicion/{id}/aprobar")
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public String aprobarEdicion(
      @PathVariable("id") Long id, // Nombre explícito
      HttpSession session,
      RedirectAttributes attrs) {
    try {
      String token = (String) session.getAttribute("accessToken");
      if (token == null) return "redirect:/login";

      solicitudService.aprobarEdicion(id, token);
      attrs.addFlashAttribute("mensaje", "Edición aprobada y aplicada.");
      attrs.addFlashAttribute("tipoMensaje", "success");
    } catch (Exception e) {
      attrs.addFlashAttribute("mensaje", "Error: " + e.getMessage());
      attrs.addFlashAttribute("tipoMensaje", "error");
    }
    return "redirect:/admin";
  }

  @PostMapping("/solicitudes/edicion/{id}/denegar")
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public String denegarEdicion(
      @PathVariable("id") Long id, // Nombre explícito
      HttpSession session,
      RedirectAttributes attrs) {
    try {
      String token = (String) session.getAttribute("accessToken");
      if (token == null) return "redirect:/login";

      solicitudService.denegarEdicion(id, token);
      attrs.addFlashAttribute("mensaje", "Edición rechazada.");
      attrs.addFlashAttribute("tipoMensaje", "info");
    } catch (Exception e) {
      attrs.addFlashAttribute("mensaje", "Error: " + e.getMessage());
      attrs.addFlashAttribute("tipoMensaje", "error");
    }
    return "redirect:/admin";
  }
}

