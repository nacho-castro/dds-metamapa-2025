package ar.utn.ba.ddsi.servicioAutenticacion.services;

import ar.utn.ba.ddsi.servicioAutenticacion.models.dtos.LoginRequest;
import ar.utn.ba.ddsi.servicioAutenticacion.models.dtos.UsuarioDTOInput;
import ar.utn.ba.ddsi.servicioAutenticacion.models.dtos.UsuarioDTOOutput;
import ar.utn.ba.ddsi.servicioAutenticacion.models.entities.usuarios.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IUsuarioService {
  public UsuarioDTOOutput registrarUsuario(UsuarioDTOInput usuario);
  public List<UsuarioDTOOutput> obtenerUsuarios();
  public UsuarioDTOOutput obtenerUsuarioPorId(Long id);
  public UsuarioDTOOutput obtenerUsuarioDesdeToken(String username);
  public UsuarioDTOOutput editarUsuario(Long id, UsuarioDTOInput nuevo);
  public UsuarioDTOOutput cambiarPassword(Long id, String newPassword);
  public UsuarioDTOOutput borrarUsuario(Long id);
  public UsuarioDTOOutput validarLogin(LoginRequest login);
}
