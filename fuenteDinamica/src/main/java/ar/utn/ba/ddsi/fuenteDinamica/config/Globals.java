package ar.utn.ba.ddsi.fuenteDinamica.config;

import lombok.Getter;

@Getter
public class Globals {
  public static final Integer edadMinimaParaPublicar = 18;
  @Getter
  public static final Integer diasParaEdicion = 7;

  public static Boolean verificadorEdad(Integer edad) {
    if (edad >= edadMinimaParaPublicar) {
      return true;
    } else {
      return false;
    }
  }

}

