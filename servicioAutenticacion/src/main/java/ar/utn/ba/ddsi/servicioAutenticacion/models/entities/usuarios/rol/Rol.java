package ar.utn.ba.ddsi.servicioAutenticacion.models.entities.usuarios.rol;


import ar.utn.ba.ddsi.servicioAutenticacion.models.entities.usuarios.Permiso;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Table(name = "rol")
public class Rol {
  private TipoRoles nombreRol;
  private List<Permiso> permisos;

  public Rol(TipoRoles rol){
    this.nombreRol = rol;
  }

  public Boolean tienePermiso(Permiso permiso){
    //todo: pepon
    return true;
  }
}
