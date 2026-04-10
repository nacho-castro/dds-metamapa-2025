package ar.utn.ba.ddsi.frontend.dto.input;

import ar.utn.ba.ddsi.frontend.dto.Permiso;
import ar.utn.ba.ddsi.frontend.dto.Rol;
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
  private Rol rol;
  private List<Permiso> permisos;
}