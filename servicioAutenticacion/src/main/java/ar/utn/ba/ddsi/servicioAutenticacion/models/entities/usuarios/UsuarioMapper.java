package ar.utn.ba.ddsi.servicioAutenticacion.models.entities.usuarios;

import ar.utn.ba.ddsi.servicioAutenticacion.models.dtos.UsuarioDTOInput;
import ar.utn.ba.ddsi.servicioAutenticacion.models.dtos.UsuarioDTOOutput;
import java.util.Collections;
import java.util.List;

public class UsuarioMapper {

  // DTO a Entidad
  public static Usuario DTOToUsuario(UsuarioDTOInput dto) {
    Usuario usuario = new Usuario();
    usuario.setNombre(dto.getNombre());
    usuario.setEmail(dto.getEmail());
    usuario.setEdad(dto.getEdad());
    return usuario;
  }

  // Entidad a DTO
  public static UsuarioDTOOutput usuarioToDTO(Usuario usuario) {
    UsuarioDTOOutput dto = new UsuarioDTOOutput();
    dto.setNombre(usuario.getNombre());
    dto.setId(usuario.getId());
    dto.setRol(usuario.getRol().toString());
    return dto;
  }

  //LISTA A DTO
  public static List<UsuarioDTOOutput> usuarioToDTO(List<Usuario> usuarios) {
    if (usuarios == null) return Collections.emptyList();

    return usuarios.stream()
        .map(UsuarioMapper::usuarioToDTO)
        .toList();
  }
}