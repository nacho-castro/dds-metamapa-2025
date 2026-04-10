package ar.utn.ba.ddsi.servicioAgregador.config;

import lombok.Getter;

@Getter
public class Globals {
  public static final Integer edadMinimaParaPublicar = 18;
  public static final Integer diasParaEdicion = 7;

  public void verificadorEdad(Integer edad) {
    if (edad >= edadMinimaParaPublicar) {
      throw new IllegalArgumentException("El edad debe ser mayor que 18");
    }
  }

  public static int getDiasParaEdicion() {
    return diasParaEdicion;
  }

}

