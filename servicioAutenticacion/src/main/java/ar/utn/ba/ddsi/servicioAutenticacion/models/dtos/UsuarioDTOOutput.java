package ar.utn.ba.ddsi.servicioAutenticacion.models.dtos;

import lombok.Data;


@Data
public class UsuarioDTOOutput {
  private Long id;
  private String nombre;
  private String rol;
  //por seguridad solo mostramos el nombre
}
