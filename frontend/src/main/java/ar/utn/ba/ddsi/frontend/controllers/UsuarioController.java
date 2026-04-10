package ar.utn.ba.ddsi.frontend.controllers;

import ar.utn.ba.ddsi.frontend.dto.input.UsuarioDTOInput;
import ar.utn.ba.ddsi.frontend.dto.output.UsuarioDTOOutput;
import ar.utn.ba.ddsi.frontend.exceptions.DuplicateMailException;
import ar.utn.ba.ddsi.frontend.exceptions.ValidationException;
import ar.utn.ba.ddsi.frontend.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*
USUARIO CONTROLLER SE ENCARGA LAS REQUESTS DE CRUD DE USUARIOS DESDE EL FRONTEND
- CREAR USUARIOS
- TODO ACTUALIZAR (PASSWORD / EMAIL)
*/

@Controller
public class UsuarioController {
  private UsuarioService usuarioService;

  public UsuarioController(UsuarioService usuarioService) {
    this.usuarioService = usuarioService;
  }

  //MOSTRAR FORMULARIO DE REGISTRO.HTML
  @GetMapping("/registro")
  public String mostrarFormularioRegistro(Model model) {
    model.addAttribute("page", "registro");
    model.addAttribute("usuario", new UsuarioDTOInput());
    return "registro";
  }

  //RECIBIR DTO DEL FORMULARIO DE REGISTRO
  @PostMapping("/registro")
  public String crearCuenta(@ModelAttribute("usuario") UsuarioDTOInput usuarioDTO,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
    try {
      UsuarioDTOOutput usuarioNuevo = usuarioService.crearCuenta(usuarioDTO);
      redirectAttributes.addFlashAttribute("mensaje", "Cuenta creada exitosamente");
      redirectAttributes.addFlashAttribute("tipoMensaje", "success");
      return "redirect:/";
    }
    catch (DuplicateMailException ex) {
      bindingResult.rejectValue("mail", "error.mail", ex.getMessage());
      model.addAttribute("page", "registro");
      return "registro";
    }
    catch (ValidationException e) {
      convertirValidationExceptionABindingResult(e, bindingResult);
      model.addAttribute("page", "registro");
      return "registro";
    }
    catch (Exception e) {
      model.addAttribute("error", "Error al crear la cuenta: " + e.getMessage());
      model.addAttribute("page", "registro");
      return "registro";
    }
  }

  private void convertirValidationExceptionABindingResult(ValidationException e, BindingResult bindingResult) {
    if(e.hasFieldErrors()) {
      e.getFieldErrors().forEach((field, error) -> bindingResult.rejectValue(field, "error." + field, error));
    }
  }
}
