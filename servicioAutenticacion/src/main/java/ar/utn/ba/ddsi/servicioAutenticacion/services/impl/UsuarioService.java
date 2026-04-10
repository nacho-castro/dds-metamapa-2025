package ar.utn.ba.ddsi.servicioAutenticacion.services.impl;

import ar.utn.ba.ddsi.servicioAutenticacion.models.dtos.LoginRequest;
import ar.utn.ba.ddsi.servicioAutenticacion.models.dtos.UsuarioDTOInput;

import ar.utn.ba.ddsi.servicioAutenticacion.models.dtos.UsuarioDTOOutput;
import ar.utn.ba.ddsi.servicioAutenticacion.models.entities.usuarios.Usuario;
import ar.utn.ba.ddsi.servicioAutenticacion.models.entities.usuarios.UsuarioMapper;
import ar.utn.ba.ddsi.servicioAutenticacion.models.entities.usuarios.password.PasswordManager;
import ar.utn.ba.ddsi.servicioAutenticacion.models.entities.usuarios.rol.TipoRoles;
import ar.utn.ba.ddsi.servicioAutenticacion.models.repositories.IUsuarioRepository;
import ar.utn.ba.ddsi.servicioAutenticacion.services.IUsuarioService;
import ar.utn.ba.ddsi.servicioAutenticacion.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements IUsuarioService {
  private IUsuarioRepository usuarioRepository;
  private final PasswordManager passwordManager;
  private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

  public UsuarioService(IUsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
    this.passwordManager = new PasswordManager();
  }
  public Usuario setearUsuario(UsuarioDTOInput dto) {
    Usuario u = UsuarioMapper.DTOToUsuario(dto);
    setearPassword(u, dto.getPassword());
    if (u.getRol() == null) {
      u.setRol(TipoRoles.CONTRIBUYENTE);
    }
    return u;
  }
  public void setearPassword(Usuario u, String p) {
    Optional.ofNullable(p)
        .filter(s -> !s.isBlank())
        .ifPresent(value -> passwordManager.setPassword(u, value));
  }

  @Override
  public UsuarioDTOOutput registrarUsuario(UsuarioDTOInput usuarioDTO) {
    Usuario usuario = setearUsuario(usuarioDTO);

    usuarioRepository.save(usuario);
    return UsuarioMapper.usuarioToDTO(usuario);
  }

  @Override
  public List<UsuarioDTOOutput> obtenerUsuarios() {
    List<Usuario> usuarios = usuarioRepository.findAll();
    return UsuarioMapper.usuarioToDTO(usuarios);
  }

  @Override
  public UsuarioDTOOutput obtenerUsuarioPorId(Long id) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    return UsuarioMapper.usuarioToDTO(usuario);
  }

  @Override
  public UsuarioDTOOutput obtenerUsuarioDesdeToken(String token) {
    //Valido el token y obtengo claims
    Claims claims = JwtUtil.validarToken(token);

    //Saco el id del usuario
    Long id = claims.get("id", Long.class);

    //Busco el usuario en la DB
    UsuarioDTOOutput usuario = obtenerUsuarioPorId(id);

    if (usuario == null) {
      throw new RuntimeException("Usuario no encontrado");
    }

    return usuario;
  }

  @Override
  public UsuarioDTOOutput editarUsuario(Long id, UsuarioDTOInput nuevo) {
    Usuario usuario = usuarioRepository.findById(id)
          .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    Usuario nuevoUser = setearUsuario(nuevo);
    usuario.updateUsuario(nuevoUser);
    usuarioRepository.save(usuario);
    return UsuarioMapper.usuarioToDTO(usuario);
  }

  @Override
  public UsuarioDTOOutput cambiarPassword(Long id, String newPassword) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    passwordManager.setPassword(usuario, newPassword);
    usuarioRepository.save(usuario);
    return UsuarioMapper.usuarioToDTO(usuario);
  }

  @Override
  public UsuarioDTOOutput borrarUsuario(Long id) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    usuarioRepository.delete(usuario);
    return UsuarioMapper.usuarioToDTO(usuario);
  }

  @Override
  public UsuarioDTOOutput validarLogin(LoginRequest login) {
    logger.info("Intentando login para el usuario: {}", login.getUsername());
    Optional<Usuario> usuarioEncontrado = usuarioRepository.validarLogin(login.getUsername(),login.getPassword());
    if(usuarioEncontrado.isPresent()){
      return UsuarioMapper.usuarioToDTO(usuarioEncontrado.get());
    } else {
      logger.warn("Usuario no encontrado: {}", login.getUsername());
      throw new RuntimeException("Usuario o contraseña incorrectos");
    }
  }
}
