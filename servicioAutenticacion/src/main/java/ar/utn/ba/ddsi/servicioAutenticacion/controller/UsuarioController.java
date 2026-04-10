package ar.utn.ba.ddsi.servicioAutenticacion.controller;

import ar.utn.ba.ddsi.servicioAutenticacion.models.dtos.PasswordInputDTO;
import ar.utn.ba.ddsi.servicioAutenticacion.models.dtos.UsuarioDTOInput;
import ar.utn.ba.ddsi.servicioAutenticacion.models.dtos.UsuarioDTOOutput;
import ar.utn.ba.ddsi.servicioAutenticacion.services.IUsuarioService;
import ar.utn.ba.ddsi.servicioAutenticacion.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
Este controlador maneja el CRUD de Usuarios
Se encarga de la persistencia de usuarios
 */

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
  private IUsuarioService usuarioService;

  public UsuarioController(IUsuarioService usuarioService) {
    this.usuarioService = usuarioService;
  }

  //CREATE:POST USUARIO
  @PostMapping()
  public ResponseEntity<UsuarioDTOOutput> crearUsuario(@RequestBody UsuarioDTOInput input) {
    UsuarioDTOOutput salida = this.usuarioService.registrarUsuario(input);
    return new ResponseEntity<>(salida, HttpStatus.CREATED);
  }

  //READ: GET USUARIOS
  @GetMapping()
  public ResponseEntity<List<UsuarioDTOOutput>> buscarTodos(){
    List<UsuarioDTOOutput> usuarios = this.usuarioService.obtenerUsuarios();
    if (usuarios.isEmpty()) {
      return ResponseEntity.noContent().build(); // 204
    }
    return ResponseEntity.ok(usuarios); // 200
  }

  //READ: GET BY ID
  @GetMapping("/{id}")
  public ResponseEntity<UsuarioDTOOutput> buscarUsuarioPorId(@PathVariable("id") Long id){
    UsuarioDTOOutput usuario = this.usuarioService.obtenerUsuarioPorId(id);
    if (usuario == null) {
      return ResponseEntity.noContent().build(); // 204
    }
    return ResponseEntity.ok(usuario); // 200
  }

  @GetMapping("/sesion")
  public ResponseEntity<UsuarioDTOOutput> obtenerUsuarioDeToken(HttpServletRequest request) {
    // 1. Tomo el token del header
    String header = request.getHeader("Authorization");
    if (header == null || !header.startsWith("Bearer ")) {
      throw new RuntimeException("Token no provisto");
    }

    String token = header.substring(7);

    UsuarioDTOOutput usuario = usuarioService.obtenerUsuarioDesdeToken(token);
    return ResponseEntity.ok(usuario);
  }

  //UPDATE: PUT USER BY ID
  @PutMapping("/{id}")
  public ResponseEntity<UsuarioDTOOutput> editarUsuario(
      @RequestBody UsuarioDTOInput input,
      @PathVariable("id") Long idUsuario){
    UsuarioDTOOutput output = this.usuarioService.editarUsuario(idUsuario, input);
    return ResponseEntity.ok(output); // 200
  }

  //UPDATE: PATCH PASSWORD USER BY ID
  @PatchMapping("/{id}")
  public ResponseEntity<UsuarioDTOOutput> cambiarPassword(
      @RequestBody PasswordInputDTO input,
      @PathVariable("id") Long idUsuario){
    UsuarioDTOOutput output = this.usuarioService.cambiarPassword(idUsuario, input.getPassword());
    return ResponseEntity.ok(output); // 200
  }

  //DELETE: DELETE User BY ID
  @DeleteMapping("/{id}")
  public ResponseEntity<UsuarioDTOOutput> eliminarUsuario(@PathVariable("id") Long id){
    UsuarioDTOOutput output = this.usuarioService.borrarUsuario(id);
    return ResponseEntity.ok(output);
  }

}
