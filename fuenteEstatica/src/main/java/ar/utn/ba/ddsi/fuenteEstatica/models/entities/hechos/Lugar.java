package ar.utn.ba.ddsi.fuenteEstatica.models.entities.hechos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Lugar {
  //DATA CLASS
  private double latitud;
  private double longitud;
  private String municipio;
  private String provincia;

  public Lugar(double latitud, double longitud) {
    this.latitud = latitud;
    this.longitud = longitud;
  }

  @Override
  public String toString() {
    return "Lugar{ "
        + "latitud=" + latitud
        + ", longitud=" + longitud + " }";
  }

  public String obtenerDireccion(){
    //SETTERS...
    return this.municipio + ", " + this.provincia;
    //TODO A partir de la latitud y longitud obtiene Municipio y Provincia
  }
}
