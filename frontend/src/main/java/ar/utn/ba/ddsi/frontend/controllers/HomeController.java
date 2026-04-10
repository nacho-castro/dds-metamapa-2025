package ar.utn.ba.ddsi.frontend.controllers;

import ar.utn.ba.ddsi.frontend.dto.output.ColeccionDTOOutput;
import ar.utn.ba.ddsi.frontend.dto.output.HechoDTOOutput;
import ar.utn.ba.ddsi.frontend.dto.output.LoginRequest;
import ar.utn.ba.ddsi.frontend.dto.output.PageDTOOutput;
import ar.utn.ba.ddsi.frontend.services.ColeccionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/*
HOME CONTROLLER SE ENCARGA DEL RUTEO PUBLICO BASICO EN EL FRONTEND
- LANDING
- INFO. LEGAL
*/

@Controller
public class HomeController {

  private final ColeccionService coleccionService;

  public HomeController(ColeccionService coleccionService) {
    this.coleccionService = coleccionService;
  }

  // LANDING PAGE
  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("page", "index");

    PageDTOOutput<ColeccionDTOOutput> coleccionesPage =
        coleccionService.obtenerColecciones(0, 1,null);

    ColeccionDTOOutput coleccionEjemplo = null;
    HechoDTOOutput hechoEjemplo = null;

    if (coleccionesPage != null && !coleccionesPage.getContent().isEmpty()) {
      coleccionEjemplo = coleccionesPage.getContent().get(0);
      model.addAttribute("coleccionEjemplo", coleccionEjemplo);

      // El método ahora pide: page, limit, id, curada, keyword, categoria, fechaInicio, fechaFin
      // Pasamos null a los filtros porque en la Home no estamos buscando nada específico.
      PageDTOOutput<HechoDTOOutput> hechosPage =
          coleccionService.obtenerHechosColeccion(
              0, 1,
              coleccionEjemplo.getId(),
              false,
              null, // keyword
              null, // categoria
              null, // fechaInicio
              null  // fechaFin
          );

      if (hechosPage != null && !hechosPage.getContent().isEmpty()) {
        hechoEjemplo = hechosPage.getContent().get(0);
        model.addAttribute("hechoEjemplo", hechoEjemplo);
      }
    }

    return "index";
  }

  @GetMapping("/acerca")
  public String acerca(Model model) {
    model.addAttribute("page", "acerca");
    return "acerca";
  }

  @GetMapping("/login")
  public String login(Model model) {
    model.addAttribute("page", "login");
    model.addAttribute("usuario", new LoginRequest());
    return "login";
  }
}

