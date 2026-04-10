package ar.utn.ba.ddsi.frontend.controllers;

import ar.utn.ba.ddsi.frontend.dto.EstadisticasDTO;
import ar.utn.ba.ddsi.frontend.services.EstadisticasService;
import org.springframework.ui.Model;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/estadisticas")
public class EstadisticasController {

  private final EstadisticasService estadisticasService;

  public EstadisticasController(EstadisticasService estadisticasService) {
    this.estadisticasService = estadisticasService;
  }

  // Mostrar el panel de admin con estadísticas cargadas
  @GetMapping
  public String mostrarPanelAdmin(Model model) {
    // Podés traer distintos tipos de estadísticas
    List<EstadisticasDTO> provinciasTop = estadisticasService.obtenerPorTipo("PROVINCIA_TOP_COLECCION");
    List<EstadisticasDTO> categoriasTop = estadisticasService.obtenerPorTipo("CATEGORIA_TOP_COLECCION");
    List<EstadisticasDTO> spam = estadisticasService.obtenerPorTipo("SOLICITUDES_SPAM_COLECCION");

    model.addAttribute("estadisticasProvincias", provinciasTop);
    model.addAttribute("estadisticasCategorias", categoriasTop);
    model.addAttribute("estadisticasSpam", spam);

    // Esto permite que el panel sepa qué pestaña o sección mostrar activa
    model.addAttribute("page", "estadisticas");

    return "estadisticas"; // Retorna el template de estadisticas
  }

  // Recalcular estadísticas
  @PostMapping("/recalcular/{coleccionId}")
  public String recalcular(@PathVariable Long coleccionId,
                           @RequestParam List<String> categorias,
                           RedirectAttributes redirectAttributes) {
    try {
      estadisticasService.recalcularTodasPorColeccion(coleccionId, categorias);
      redirectAttributes.addFlashAttribute("mensaje", "Estadísticas recalculadas correctamente.");
      redirectAttributes.addFlashAttribute("tipoMensaje", "success");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("mensaje", "Error al recalcular estadísticas: " + e.getMessage());
      redirectAttributes.addFlashAttribute("tipoMensaje", "error");
    }

    return "redirect:/admin/estadisticas";
  }

  // Exportar CSV (si querés que se descargue desde el panel)
  @GetMapping(value = "/export/{tipo}", produces = "text/csv")
  public ResponseEntity<ByteArrayResource> exportar(@PathVariable String tipo) {
    List<EstadisticasDTO> datos = estadisticasService.obtenerPorTipo(tipo);
    StringBuilder sb = new StringBuilder("clave,valor,fecha\n");
    datos.forEach(e -> sb.append(e.getClave())
        .append(",")
        .append(e.getValor())
        .append(",")
        .append(e.getFechaCalculo())
        .append("\n"));

    ByteArrayResource resource = new ByteArrayResource(sb.toString().getBytes(StandardCharsets.UTF_8));
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + tipo + ".csv")
        .contentType(MediaType.parseMediaType("text/csv"))
        .body(resource);
  }
}
