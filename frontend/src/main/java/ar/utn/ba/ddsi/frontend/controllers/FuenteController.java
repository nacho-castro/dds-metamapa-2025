package ar.utn.ba.ddsi.frontend.controllers;


import ar.utn.ba.ddsi.frontend.dto.input.EditFuenteDTO;
import ar.utn.ba.ddsi.frontend.dto.input.FuenteDTOInput;
import ar.utn.ba.ddsi.frontend.dto.output.FuenteDTOOutput;
import ar.utn.ba.ddsi.frontend.services.FuenteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/fuentes")
public class FuenteController {

  private final FuenteService fuenteService;

  public FuenteController(FuenteService fuenteService) {
    this.fuenteService = fuenteService;
  }

  @GetMapping
  public String fuentes(Model model) {

    List<FuenteDTOOutput> fuentes;

    try {
      fuentes = fuenteService.obtenerFuentes();
    } catch (Exception e) {
      fuentes = List.of();
    }

    model.addAttribute("fuentes", fuentes);
    model.addAttribute("fuentesDisponibles", fuentes);
    model.addAttribute("fuente", new FuenteDTOInput());
    model.addAttribute("page", "fuentes");

    // === ROL DEL USUARIO (MISMA LÓGICA QUE COLECCIONES) ===
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
      boolean isAdmin = auth.getAuthorities().stream()
          .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));
      model.addAttribute("rol", isAdmin ? "ADMINISTRADOR" : "USUARIO");
    } else {
      model.addAttribute("rol", "VISUALIZADOR");
    }

    return "fuentes";
  }


  @PostMapping
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public String crearFuente(@ModelAttribute("fuente") FuenteDTOInput fuenteDTOInput,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

    String token = (String) session.getAttribute("accessToken");

    if (token == null) {
      redirectAttributes.addFlashAttribute("error", "No estás autenticado.");
      return "redirect:/colecciones";
    }

    try {
      fuenteService.crearFuente(fuenteDTOInput, token);
      redirectAttributes.addFlashAttribute(
          "mensaje", "Fuente creada exitosamente"
      );
      redirectAttributes.addFlashAttribute("tipoMensaje", "success");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute(
          "error", "Error al crear fuente: " + e.getMessage()
      );
      redirectAttributes.addFlashAttribute("tipoMensaje", "error");
    }

    return "redirect:/fuentes";
  }

  @PostMapping("/{id}/editar")
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public String editarFuente(@PathVariable("id") Long id,
                             @ModelAttribute("fuente") EditFuenteDTO dto,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
    try {
      String token = (String) session.getAttribute("accessToken");
      fuenteService.editarFuente(id, dto, token);
      redirectAttributes.addFlashAttribute("mensaje", "Fuente editada correctamente");
      return "redirect:/fuentes";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
      return "redirect:/fuentes";
    }
  }


  // ==========================
  // ELIMINAR FUENTE
  // ==========================
  @PostMapping("/{id}/eliminar")
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public String eliminarFuente(@PathVariable("id") Long id,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

    try {
      String token = (String) session.getAttribute("accessToken");

      if (token == null) {
        redirectAttributes.addFlashAttribute("error", "No estás autenticado. Por favor, inicia sesión nuevamente.");
        redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        return "redirect:/fuentes";
      }

      fuenteService.eliminarFuente(id, token);

      redirectAttributes.addFlashAttribute("mensaje", "Fuente eliminada exitosamente");
      redirectAttributes.addFlashAttribute("tipoMensaje", "success");
      return "redirect:/fuentes";

    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error al eliminar fuente: " + e.getMessage());
      redirectAttributes.addFlashAttribute("tipoMensaje", "error");
      return "redirect:/fuentes";
    }
  }

}
