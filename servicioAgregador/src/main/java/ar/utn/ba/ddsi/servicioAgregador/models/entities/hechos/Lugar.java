package ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lugar")
public class Lugar {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //DATA CLASS
  @Column(name = "latitud", nullable = false)
  private double latitud;

  @Column(name = "longitud", nullable = false)
  private double longitud;

  @Column(name = "municipio", nullable = true)
  private String municipio;

  @Column(name = "provincia", nullable = true)
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
