package ar.utn.ba.ddsi.frontend.controllers;

import ar.utn.ba.ddsi.frontend.dto.output.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/*
Intermediario entre peticiones del front y SpringSecurity
Recibe usuario y contraseña del frontend (modal).
Crea un UsernamePasswordAuthenticationToken con esos datos

Llama a authManager.authenticate(...), que internamente pasa por CustomAuthProvider:
- Valida credenciales contra el servicio externo
- Obtiene tokens
- Guarda roles y permisos en la sesión HTTP

Guarda la autenticación en:
- SecurityContextHolder → Spring Security sabe que el usuario está autenticado.
- HttpSession → permite que la sesión persista entre requests HTTP.
 */

@RestController
@RequestMapping("/auth")
public class AuthController {

  private static final Logger log = LoggerFactory.getLogger(AuthController.class);
  private final AuthenticationManager authenticationManager;

  public AuthController(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @PostMapping
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
    try {
      // Create authentication token
      UsernamePasswordAuthenticationToken authToken =
          new UsernamePasswordAuthenticationToken(
              loginRequest.getUsername(),
              loginRequest.getPassword()
          );

      // Authenticate - this calls CustomAuthProvider.authenticate() ONCE
      Authentication authentication = authenticationManager.authenticate(authToken);

      // Set authentication in security context
      SecurityContext securityContext = SecurityContextHolder.getContext();
      securityContext.setAuthentication(authentication);

      // Save security context to session
      HttpSession session = request.getSession(true);
      session.setAttribute(
          HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
          securityContext
      );

      return ResponseEntity.ok(Map.of(
          "success", true,
          "username", authentication.getName(),
          "authorities", authentication.getAuthorities()
      ));

    } catch (AuthenticationException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("success", false, "message", "Credenciales inválidas"));
    }
  }

  @GetMapping
  public ResponseEntity<?> getCurrentUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    return ResponseEntity.ok(Map.of(
        "username", auth.getName(),
        "authorities", auth.getAuthorities()
    ));
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    SecurityContextHolder.clearContext();
    log.info("User logged out successfully");
    return ResponseEntity.ok(Map.of("success", true));
  }
}