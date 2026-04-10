package ar.utn.ba.ddsi.frontend.controllers;

import ar.utn.ba.ddsi.frontend.dto.input.CriterioDTOInput;
import ar.utn.ba.ddsi.frontend.dto.output.CriterioDTOOutput;
import ar.utn.ba.ddsi.frontend.services.CriterioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/criterios")
public class CriterioController {
    private final CriterioService service;
    private final CriterioService criterioService;

    public CriterioController(CriterioService service, CriterioService criterioService) {
        this.service = service;
        this.criterioService = criterioService;
    }

    @PostMapping()
    public String crearCriterio(@ModelAttribute("criterio")
                                CriterioDTOInput criterioDTOInput,
                                HttpSession session,
                                RedirectAttributes redirectAttributes)
    {
        try {
            System.out.println("DTO recibido: " + criterioDTOInput);
            System.out.println("=== CREANDO CRITERIO ===");
            System.out.println("Tipo Criterio: " + criterioDTOInput.getTipo());
            System.out.println("Valor1: " + criterioDTOInput.getValor1());
            System.out.println("Valo2: " + criterioDTOInput.getValor2());

            if (criterioDTOInput.getTipo() == null || criterioDTOInput.getTipo().isEmpty()) {
                System.err.println("❌ ERROR: tipo es null o vacío!");
                redirectAttributes.addFlashAttribute("error", "El campo Tipo es obligatorio");
                redirectAttributes.addFlashAttribute("tipoMensaje", "error");
                return "redirect:/colecciones";
            }

            String token = (String) session.getAttribute("accessToken");
            if (token == null) {
                redirectAttributes.addFlashAttribute("error", "No estás autenticado.");
                return "redirect:/colecciones";
            }

            CriterioDTOOutput criterioCreado = service.crearCriterio(criterioDTOInput, token);
            redirectAttributes.addFlashAttribute("mensaje", "El criterio fue creado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
            return "redirect:/colecciones";
        }
        catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Error al crear criterio: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
            return "redirect:/colecciones";
        }
    }
    @GetMapping()
    public String criterios(Model model){
        List<CriterioDTOOutput> criterios = criterioService.obtenerCriterios();
        model.addAttribute("criterios", criterios);
        model.addAttribute("criterio", new CriterioDTOInput());
        model.addAttribute("criteriosDisponibles", criterios);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));
            model.addAttribute("rol", isAdmin ? "ADMINISTRADOR" : "USUARIO");
        } else {
            model.addAttribute("rol", "VISUALIZADOR");
        }

        return "criterios";
    }

}
