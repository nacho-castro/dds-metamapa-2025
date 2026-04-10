package ar.utn.ba.ddsi.frontend.controllers;

import ar.utn.ba.ddsi.frontend.dto.input.*;
import ar.utn.ba.ddsi.frontend.dto.output.*;
import ar.utn.ba.ddsi.frontend.services.ColeccionService;
import ar.utn.ba.ddsi.frontend.services.CriterioService;
import ar.utn.ba.ddsi.frontend.services.FuenteService;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
COLECCION CONTROLLER SE ENCARGA DEL CRUD DE COLECCIONES
- OBTENER COLECCIONES Y SUS HECHOS
- CREAR COLECCION (ADMIN)
- ELIMINAR COLECCION (ADMIN)
- EDITAR COLECCION (ADMIN)
 */

@Controller
@RequestMapping("/colecciones")
public class ColeccionController {
  private final ColeccionService coleccionService;
  private final FuenteService fuenteService;
  private final CriterioService criterioService;

  public ColeccionController(ColeccionService coleccionService, FuenteService fuenteService, CriterioService criterioService) {
    this.coleccionService = coleccionService;
    this.fuenteService = fuenteService;
    this.criterioService = criterioService;
  }

  @GetMapping()
  public String colecciones(
      Model model,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "limit", defaultValue = "9") int limit,
      @RequestParam(name = "titulo", required = false) String titulo, //parámetro de búsqueda

      @RequestParam(name = "pageH", required = false) Integer pageH,
      @RequestParam(name = "limitH", required = false) Integer limitH,
      @RequestParam(name = "tituloH", required = false) String tituloH,
      @RequestParam(name = "descripcionH", required = false) String descripcionH,
      @RequestParam(name = "fechaDesdeH", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDesdeH,
      @RequestParam(name = "fechaHastaH", required = false)
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaHastaH
  ) {

    PageDTOOutput<ColeccionDTOOutput> colecciones =
        coleccionService.obtenerColecciones(page, limit, titulo);

    PageDTOOutput<HechoDTOOutput> hechos =
        coleccionService.obtenerHechos(pageH, limitH, tituloH, descripcionH, fechaDesdeH, fechaHastaH);

    model.addAttribute("colecciones", colecciones.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", colecciones.getTotalPages());
    model.addAttribute("page", "colecciones");
    model.addAttribute("titulo", titulo);
    model.addAttribute("hechos", hechos.getContent()); //mapa

    // ROL del usuario
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
      boolean isAdmin = auth.getAuthorities().stream()
          .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));
      model.addAttribute("rol", isAdmin ? "ADMINISTRADOR" : "USUARIO");
    } else {
      model.addAttribute("rol", "VISUALIZADOR");
    }
    List<FuenteDTOOutput> fuentes = null;
    try {
      System.out.println("=== OBTENIENDO FUENTES ===");
      fuentes = fuenteService.obtenerFuentes();
      System.out.println("Fuentes obtenidas: " + (fuentes != null ? fuentes.size() : "NULL"));

      if (fuentes != null && !fuentes.isEmpty()) {
        System.out.println("=== FUENTES DISPONIBLES ===");
        System.out.println("Total fuentes: " + fuentes.size());
        fuentes.forEach(f -> {
          System.out.println("- ID: " + f.getId() +
                  ", Tipo: " + f.getTipoFuente() +
                  ", Path: " + f.getPath());
        });
      } else {
        System.out.println("⚠️ No hay fuentes disponibles o la lista es null");
      }

    } catch (Exception e) {
      System.err.println("Error al obtener fuentes: " + e.getMessage());
      model.addAttribute("fuentesDisponibles", List.of()); // Lista vacía
    }
    if (fuentes == null) {
      fuentes = new ArrayList<>();
      System.out.println("⚠️ Inicializando lista de fuentes como ArrayList vacío");
    }

    List<CriterioDTOOutput> criterios = null;
    try {
      System.out.println("=== OBTENIENDO CRITERIOS ===");
      criterios = criterioService.obtenerCriterios();
      System.out.println("Criterios obtenidos: " + (criterios != null ? criterios.size() : "NULL"));

      if (criterios != null && !criterios.isEmpty()) {
        System.out.println("=== CRITERIOS DISPONIBLES ===");
        System.out.println("Total criterios: " + criterios.size());
        criterios.forEach(c -> {
          System.out.println("- ID: " + c.getId() +
                  ", categoria: " + c.getCategoria() +
                  ", titulo: " + c.getTitulo());
        });
      } else {
        System.out.println("⚠️ No hay criterios disponibles o la lista es null");
      }

    } catch (Exception e) {
      System.err.println("Error al obtener criterios: " + e.getMessage());
      model.addAttribute("criteriosDisponibles", List.of()); // Lista vacía
    }
    if (criterios == null) {
      criterios = new ArrayList<>();
      System.out.println("⚠️ Inicializando lista de criterios como ArrayList vacío");
    }

    model.addAttribute("fuentesDisponibles", fuentes);
    System.out.println("✅ fuentesDisponibles agregado al modelo con " + fuentes.size() + " elementos");

    model.addAttribute("criteriosDisponibles", criterios);
    System.out.println("✅ criteriosDisponibles agregado al modelo con " + criterios.size() + " elementos");

    model.addAttribute("coleccion", new ColeccionDTOInput());
    model.addAttribute("hecho", new HechoDTOInput());
    model.addAttribute("fuente", new FuenteDTOInput());
    model.addAttribute("criterio", new CriterioDTOInput());

    return "colecciones";
  }

  @GetMapping("/{id}")
  public String hechos(Model model,
                       @PathVariable("id") Long id,
                       @RequestParam(name = "page", defaultValue = "0") int page,
                       @RequestParam(name = "limit", defaultValue = "6") int limit,
                       @RequestParam(name = "curada", defaultValue = "false") boolean curada,
                       // Parametros del Filtro
                       @RequestParam(name = "keyword", required = false) String keyword,
                       @RequestParam(name = "categoria", required = false) String categoria,
                       @RequestParam(name = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                       @RequestParam(name = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

    // Llamada al servicio con la nueva firma
    PageDTOOutput<HechoDTOOutput> hechosPage = coleccionService.obtenerHechosColeccion(
        page, limit, id, curada, keyword, categoria, fechaInicio, fechaFin
    );

    ColeccionDTOOutput coleccion = coleccionService.obtenerColeccionPorId(id);

    // === NUEVA LÓGICA DE ROLES MANUAL ===
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    boolean esAdmin = false;
    boolean esContribuyente = false;

    if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
      // Verificamos los roles revisando las autoridades del usuario
      esAdmin = auth.getAuthorities().stream()
          .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR") || a.getAuthority().equals("ADMINISTRADOR"));

      esContribuyente = auth.getAuthorities().stream()
          .anyMatch(a -> a.getAuthority().equals("ROLE_CONTRIBUYENTE") || a.getAuthority().equals("CONTRIBUYENTE"));
    }

    // Pasamos booleanos simples a la vista
    model.addAttribute("esAdmin", esAdmin);
    model.addAttribute("esContribuyente", esContribuyente);

    model.addAttribute("hechos", hechosPage.getContent());
    model.addAttribute("coleccionNombre", coleccion.getTitulo());
    model.addAttribute("coleccionDescripcion", coleccion.getDescripcion());
    model.addAttribute("coleccionId", id);

    // Paginación
    model.addAttribute("currentPage", hechosPage.getPage());
    model.addAttribute("totalPages", hechosPage.getTotalPages());
    model.addAttribute("limit", limit);
    model.addAttribute("page", "hechos");

    // Mantener filtros en la vista (para paginación y inputs)
    model.addAttribute("curada", curada);
    model.addAttribute("keyword", keyword);
    model.addAttribute("categoriaSeleccionada", categoria);
    model.addAttribute("fechaInicio", fechaInicio);
    model.addAttribute("fechaFin", fechaFin);

    return "hechos";
  }

  @PostMapping()
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public String crearColeccion(@ModelAttribute("coleccion") ColeccionDTOInput coleccionDTOInput,
                               HttpSession session,
                               Model model,
                               RedirectAttributes redirectAttributes) {
    try {
      System.out.println("=== DTO RECIBIDO ===");
      System.out.println("Título: " + coleccionDTOInput.getTitulo());
      System.out.println("Descripción: " + coleccionDTOInput.getDescripcion());
      System.out.println("Algoritmo: " + coleccionDTOInput.getAlgoritmoConsenso());
      System.out.println("Cantidad de fuentes: " + (coleccionDTOInput.getFuentes() != null ? coleccionDTOInput.getFuentes().size() : 0));
      System.out.println("DTO recibido: " + coleccionDTOInput);

      String token = (String) session.getAttribute("accessToken");
      if (token == null) {
        redirectAttributes.addFlashAttribute("error", "No estás autenticado.");
        return "redirect:/colecciones";
      }
      ColeccionDTOOutput coleccionNueva = coleccionService.crearColeccion(coleccionDTOInput, token);
      redirectAttributes.addFlashAttribute("mensaje", "Colección creada exitosamente");
      redirectAttributes.addFlashAttribute("tipoMensaje", "success");
      return "redirect:/colecciones";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error al crear colección: " + e.getMessage());
      redirectAttributes.addFlashAttribute("tipoMensaje", "error");
      return "redirect:/colecciones";
    }
  }

  @PostMapping("/{id}/editar")
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public String editarColeccion(@ModelAttribute("coleccion") EditColeccionDTO editColeccionDTO,
                                @PathVariable("id") Long id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes
  ) {
    try {
      String token = (String) session.getAttribute("accessToken");
      if (token == null) {
        redirectAttributes.addFlashAttribute("error", "No estás autenticado.");
        return "redirect:/colecciones";
      }
      ColeccionDTOOutput coleccionNueva = coleccionService.editarColeccion(id, editColeccionDTO, token);
      redirectAttributes.addFlashAttribute("mensaje", "Colección editada exitosamente");
      redirectAttributes.addFlashAttribute("tipoMensaje", "success");
      return "redirect:/colecciones";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error al editar colección: " + e.getMessage());
      redirectAttributes.addFlashAttribute("tipoMensaje", "error");
      return "redirect:/colecciones";
    }
  }

  @PostMapping("/{id}/eliminar")
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public String eliminarColeccion(@PathVariable("id") Long id,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
    try {
      String token = (String) session.getAttribute("accessToken");

      if (token == null) {
        redirectAttributes.addFlashAttribute("error", "No estás autenticado. Por favor, inicia sesión nuevamente.");
        redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        return "redirect:/colecciones";
      }

      ColeccionDTOOutput coleccionBorrada = coleccionService.eliminarColeccion(id, token);
      redirectAttributes.addFlashAttribute("mensaje", "Colección eliminada exitosamente");
      redirectAttributes.addFlashAttribute("tipoMensaje", "success");
      return "redirect:/colecciones";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error al eliminar colección: " + e.getMessage());
      redirectAttributes.addFlashAttribute("tipoMensaje", "error");
      return "redirect:/colecciones";
    }
  }

  @PostMapping("/agregar-fuente")
  @PreAuthorize("hasRole('ADMINISTRADOR')")
  public String agregarFuente(@RequestParam Long idColeccion,
                              @RequestParam Long idFuente,
                              HttpSession session,
                              RedirectAttributes redirectAttributes
  ) {
    try {
      String token = (String) session.getAttribute("accessToken");
      if (token == null) {
        redirectAttributes.addFlashAttribute("error", "No estás autenticado.");
        return "redirect:/colecciones/" + idColeccion;
      }
      coleccionService.agregarFuente(idColeccion, idFuente, token);
      redirectAttributes.addFlashAttribute("mensaje", "Fuente agregada exitosamente");
      redirectAttributes.addFlashAttribute("tipoMensaje", "success");
      return "redirect:/colecciones/" + idColeccion;
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error al agregar fuente: " + e.getMessage());
      redirectAttributes.addFlashAttribute("tipoMensaje", "error");
      return "redirect:/colecciones/" + idColeccion;
    }
  }

}
