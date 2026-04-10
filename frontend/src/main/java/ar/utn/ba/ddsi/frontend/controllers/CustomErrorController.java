package ar.utn.ba.ddsi.frontend.controllers;

import ar.utn.ba.ddsi.frontend.services.AuthApiService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/*
ERROR CONTROLLER SE ENCARGA DEL RUTEO DE ERRORES
- 403
- 404
*/

@Controller
public class CustomErrorController implements ErrorController {

  private static final Logger log = LoggerFactory.getLogger(AuthApiService.class);

  @RequestMapping("/error")
  public String handleError(HttpServletRequest request) {
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

    log.info("El estatus es: " + status.toString());
    int statusCode = Integer.parseInt(status.toString());

    if (statusCode == HttpStatus.FORBIDDEN.value()) {
      return "error/403";
    } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
      return "error/404";
    } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
      return "error/401";
    } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
      return "error/500";
    }

    // Error genérico
    return "error/404";
  }
}
