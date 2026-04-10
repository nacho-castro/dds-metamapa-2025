package ar.utn.ba.ddsi.servicioAutenticacion.services.impl;

import ar.utn.ba.ddsi.servicioAutenticacion.exceptions.NotFoundException;
import ar.utn.ba.ddsi.servicioAutenticacion.models.dtos.LoginRequest;
import ar.utn.ba.ddsi.servicioAutenticacion.models.dtos.RolesPermisosDTO;
import ar.utn.ba.ddsi.servicioAutenticacion.models.entities.usuarios.Usuario;
import ar.utn.ba.ddsi.servicioAutenticacion.models.entities.usuarios.password.PasswordManager;
import ar.utn.ba.ddsi.servicioAutenticacion.models.repositories.IUsuarioRepository;
import ar.utn.ba.ddsi.servicioAutenticacion.utils.JwtUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
Servicio encargado de autenticar usuarios y generar los tokens
Tiene contacto con la base de datos
 */

@Service
public class LoginService {
  private final IUsuarioRepository usuariosRepository;
  private final PasswordManager passwordManager;

  public LoginService(IUsuarioRepository usuariosRepository) {
    this.usuariosRepository = usuariosRepository;
    this.passwordManager = new PasswordManager();
  }

  public Usuario autenticarUsuario(String username, String password) {
    Optional<Usuario> usuarioOpt = usuariosRepository.findByEmail(username); //username

    if (usuarioOpt.isEmpty()) {
      throw new NotFoundException("Usuario", username);
    }

    Usuario usuario = usuarioOpt.get();

    if (!passwordManager.passwordMatches(usuario, password)) {
      throw new BadCredentialsException("Credenciales inválidas");
    }

    return usuario;
  }

  public RolesPermisosDTO obtenerRolesYPermisosUsuario(String username) {
    Optional<Usuario> usuarioOpt = usuariosRepository.findByEmail(username);

    if (usuarioOpt.isEmpty()) {
      throw new NotFoundException("Usuario", username);
    }

    Usuario usuario = usuarioOpt.get();

    //Actualmente nuestro sistema solo posee rol
    return RolesPermisosDTO.builder()
        .username(usuario.getEmail())
        .rol(usuario.getRol())
        .build();
  }
}
