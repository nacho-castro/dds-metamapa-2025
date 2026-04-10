package ar.utn.ba.ddsi.servicioAutenticacion.controller;

import ar.utn.ba.ddsi.servicioAutenticacion.exceptions.NotFoundException;
import ar.utn.ba.ddsi.servicioAutenticacion.models.dtos.AuthResponseDTO;
import ar.utn.ba.ddsi.servicioAutenticacion.models.dtos.LoginRequest;
import ar.utn.ba.ddsi.servicioAutenticacion.models.dtos.RefreshRequest;
import ar.utn.ba.ddsi.servicioAutenticacion.models.dtos.RolesPermisosDTO;
import ar.utn.ba.ddsi.servicioAutenticacion.models.dtos.TokenResponse;
import ar.utn.ba.ddsi.servicioAutenticacion.models.entities.usuarios.Usuario;
import ar.utn.ba.ddsi.servicioAutenticacion.services.impl.LoginService;
import ar.utn.ba.ddsi.servicioAutenticacion.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
Este controlador recibe las solicitudes HTTP para login y refresh
Retorna tokens, roles y permisos del usuario
 */

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:8086") // frontend
public class LoginController {
  private static final Logger log = LoggerFactory.getLogger(LoginController.class);
  private final LoginService loginService;

  public LoginController(LoginService usuarioService) {
    this.loginService = usuarioService;
  }

  @PostMapping()
  public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequest login) {
    try {

      String username = login.getUsername();
      String password = login.getPassword();

      // Validación básica de credenciales
      if (username == null || username.trim().isEmpty() ||
          password == null || password.trim().isEmpty()) {
        return ResponseEntity.badRequest().build();
      }

      // Service que valida usuario y contraseña
      Usuario usuario = loginService.autenticarUsuario(username, password);

      // Generar tokens
      String accessToken = JwtUtil.generarAccessToken(
          usuario.getId(),
          usuario.getEmail(),
          usuario.getRol().name()
      );

      String refreshToken = JwtUtil.generarRefreshToken(
          usuario.getId(),
          usuario.getEmail(),
          usuario.getRol().name()
      );

      AuthResponseDTO response = AuthResponseDTO.builder()
          .accessToken(accessToken)
          .refreshToken(refreshToken)
          .build();

      return ResponseEntity.ok(response);

    } catch (RuntimeException e) {
      // Devuelve 401 Unauthorized si falla el login
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(null);
    }
  }

  @PostMapping("/refresh")
  public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest request) {
    try {
      Claims claims = JwtUtil.validarToken(request.getRefreshToken());

      // Validar que el token sea de tipo refresh
      if (!"refresh".equals(claims.get("type"))) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }

      Long id = claims.get("id", Long.class);
      String username = claims.get("username", String.class);
      String rol = claims.get("rol", String.class);

      // Crear nuevo access token
      String newAccess = JwtUtil.generarAccessToken(id, username, rol);

      return ResponseEntity.ok(new TokenResponse(newAccess, request.getRefreshToken()));

    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/user/roles-permisos")
  public ResponseEntity<RolesPermisosDTO> getUserRolesAndPermissions(Authentication authentication) {
    try {
      String username = authentication.getName();
      RolesPermisosDTO response = loginService.obtenerRolesYPermisosUsuario(username);
      return ResponseEntity.ok(response);
    } catch (NotFoundException e) {
      log.error("Usuario no encontrado", e);
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      log.error("Error al obtener roles y permisos del usuario", e);
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/validation")
  public ResponseEntity<Void> validarToken(@RequestHeader("Authorization") String authHeader) {
    try {
      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }

      String token = authHeader.substring(7);
      JwtUtil.validarToken(token);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

}
