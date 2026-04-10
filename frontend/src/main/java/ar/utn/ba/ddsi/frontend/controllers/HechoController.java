package ar.utn.ba.ddsi.frontend.controllers;

import ar.utn.ba.ddsi.frontend.dto.HechoDTO;
import ar.utn.ba.ddsi.frontend.dto.input.SolicitudEdicionDTOInput;
import ar.utn.ba.ddsi.frontend.dto.input.SolicitudInputDTO;
import ar.utn.ba.ddsi.frontend.dto.output.SolicitudAdminDTO;
import ar.utn.ba.ddsi.frontend.services.HechoService;
import ar.utn.ba.ddsi.frontend.services.SolicitudService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/hechos")
public class HechoController {
  private static final Logger log = LoggerFactory.getLogger(HechoController.class);
  private HechoService hechoService;
  private SolicitudService solicitudService;

  public HechoController(HechoService hechoService, SolicitudService solicitudService) {
    this.hechoService = hechoService;
    this.solicitudService = solicitudService;
  }

  //RECIBIR DTO DEL FORMULARIO DE CARGAR HECHO
  @PostMapping()
  public String crearHecho(@ModelAttribute("hecho") HechoDTO hechoDTO,
                           Model model,
                           RedirectAttributes redirectAttributes) {
    try {
      HechoDTO hechoNuevo = hechoService.crearHecho(hechoDTO);
      redirectAttributes.addFlashAttribute("mensaje", "Hecho creado exitosamente");
      redirectAttributes.addFlashAttribute("tipoMensaje", "success");
      return "redirect:/colecciones";
    } catch (Exception e) {
      model.addAttribute("error", "Error al crear hecho: " + e.getMessage());
      model.addAttribute("page", "colecciones");
      return "colecciones";
    }
  }

  @PostMapping("/{id}/editar")
  public String editarHecho(@PathVariable("id") Long id,
                            @RequestParam("idColeccion") Long idColeccion,
                            @ModelAttribute("hecho") HechoDTO hechoDTO,
                            HttpSession session, // <--- 1. Inyectar sesión
                            RedirectAttributes redirectAttributes) {
    try {
      // 2. Obtener Token
      String token = (String) session.getAttribute("accessToken");
      if (token == null) return "redirect:/login";

      // 3. Pasar token al servicio
      hechoService.editarHecho(id, hechoDTO, token);

      redirectAttributes.addFlashAttribute("mensaje", "Hecho editado exitosamente");
      redirectAttributes.addFlashAttribute("tipoMensaje", "success");
      return "redirect:/colecciones/" + idColeccion;

    } catch (Exception e) {
      // ... manejo de error igual ...
      redirectAttributes.addFlashAttribute("error", "Error al editar: " + e.getMessage());
      return "redirect:/colecciones/" + idColeccion;
    }
  }

  @PostMapping("/{id}/eliminar")
  public String eliminarHecho(@PathVariable("id") Long id,
                              @RequestParam(name = "idColeccion") Long idColeccion,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
    try {
      System.out.println(">>> INTENTANDO ELIMINAR HECHO ID: " + id); // <--- DEBUG 1

      String token = (String) session.getAttribute("accessToken");
      if (token == null) {
        System.out.println(">>> ERROR: TOKEN NULO"); // <--- DEBUG 2
        return "redirect:/login";
      }

      hechoService.eliminarHecho(id, token);

      redirectAttributes.addFlashAttribute("mensaje", "Hecho eliminado exitosamente");
      return "redirect:/colecciones/" + idColeccion;

    } catch (Exception e) {
      // ESTO ES LO QUE NECESITAMOS VER
      System.err.println(">>> ERROR FATAL AL ELIMINAR:");
      e.printStackTrace();

      redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
      return "redirect:/colecciones/" + idColeccion;
    }
  }

  @PostMapping("/solicitud-eliminacion")
  public String crearSolicitudEliminacion(@ModelAttribute("solicitud") SolicitudInputDTO solicitudInputDTO,
                                          HttpSession session, // <--- Importante: Inyectar la sesión
                                          RedirectAttributes redirectAttributes) {
    try {
      // 1. Obtener el token con el nombre CORRECTO ("accessToken")
      String token = (String) session.getAttribute("accessToken");

      if (token == null) {
        redirectAttributes.addFlashAttribute("mensaje", "Error: No hay sesión activa. Por favor inicia sesión nuevamente.");
        redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        return "redirect:/colecciones";
      }

      log.info("VOY A EJECUTAR SERVICIO DE SOLI CONTROLLER");
      log.info("Motivo: " + solicitudInputDTO.getMotivoBorrado());

      // 2. Pasar el token al servicio
      SolicitudAdminDTO soli = solicitudService.crearSolicitud(solicitudInputDTO, token);

      redirectAttributes.addFlashAttribute("mensaje", "Solicitud enviada correctamente");
      redirectAttributes.addFlashAttribute("tipoMensaje", "success");

    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("mensaje", "Error al enviar solicitud: " + e.getMessage());
      redirectAttributes.addFlashAttribute("tipoMensaje", "error");
    }
    return "redirect:/colecciones";
  }

  @PostMapping("/solicitudes/edicion")
  public String crearSolicitudEdicion(
      // CORRECCIÓN: Agregamos 'name' explícitamente a todos los parámetros
      @RequestParam(name = "idHecho") Long idHecho,
      @RequestParam(name = "idColeccion") Long idColeccion,
      @RequestParam(name = "nuevoTitulo") String nuevoTitulo,
      @RequestParam(name = "nuevaDescripcion") String nuevaDescripcion,
      @RequestParam(name = "nuevaCategoria") String nuevaCategoria,
      @RequestParam(name = "motivo", required = false) String motivo,
      HttpSession session,
      RedirectAttributes redirectAttributes) {
    try {
      String token = (String) session.getAttribute("accessToken");
      if (token == null) return "redirect:/login";

      // Crear DTO
      SolicitudEdicionDTOInput dto = new SolicitudEdicionDTOInput();
      dto.setIdHecho(idHecho);
      dto.setNuevoTitulo(nuevoTitulo);
      dto.setNuevaDescripcion(nuevaDescripcion);
      dto.setNuevaCategoria(nuevaCategoria);
      dto.setMotivo(motivo);

      solicitudService.crearSolicitudEdicion(dto, token);

      redirectAttributes.addFlashAttribute("mensaje", "Solicitud de edición enviada.");
      redirectAttributes.addFlashAttribute("tipoMensaje", "success");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("mensaje", "Error: " + e.getMessage());
      redirectAttributes.addFlashAttribute("tipoMensaje", "error");
    }
    return "redirect:/colecciones/" + idColeccion;
  }

  @GetMapping("/{id}/editar")
  public String mostrarFormularioEdicion(@PathVariable("id") Long id,
                                         @RequestParam(name = "idColeccion", required = false) Long idColeccion,
                                         Model model) {
    try {
      HechoDTO hecho = hechoService.obtenerHechoPorId(id);

      model.addAttribute("hecho", hecho);
      model.addAttribute("idColeccion", idColeccion); // Pasamos esto para el botón "Cancelar"

      return "editar_hecho"; // Nombre exacto del archivo HTML en templates/
    } catch (Exception e) {
      e.printStackTrace(); // <--- IMPORTANTE: Mira la consola de IntelliJ/Eclipse cuando falles
      return "redirect:/colecciones/" + (idColeccion != null ? idColeccion : "");
    }

  }
}



