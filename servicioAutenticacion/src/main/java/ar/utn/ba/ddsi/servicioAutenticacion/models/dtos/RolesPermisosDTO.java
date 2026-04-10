package ar.utn.ba.ddsi.servicioAutenticacion.models.dtos;

import ar.utn.ba.ddsi.servicioAutenticacion.models.entities.Permiso;
import ar.utn.ba.ddsi.servicioAutenticacion.models.entities.usuarios.rol.TipoRoles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolesPermisosDTO {
  private String username;
  private TipoRoles rol;
  private List<Permiso> permisos;
}