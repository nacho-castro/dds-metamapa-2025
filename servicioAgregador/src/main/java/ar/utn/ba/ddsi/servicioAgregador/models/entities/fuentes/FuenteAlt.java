package ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Component
@NoArgsConstructor
@Entity
@Table(name = "fuente")
public class FuenteAlt {
  //ESTA CLASE ES NECESARIA PARA MATCHEAR ENUM Y ATRIBUTOS (PATH)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nombre")
  private String nombre;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_fuente")
  private TipoFuente tipo;

  @Column(name = "path")
  private String path;

  @Column(name = "path_info")
  private String pathInfo;

  @Column(name = "cant_hechos")
  private Integer cantHechos;

  @ManyToMany(mappedBy = "fuenteDeOrigen", cascade = CascadeType.REMOVE)
  private List<Hecho> hechos = new ArrayList<>();

  public void update(FuenteAlt nueva) {
    this.nombre = nueva.getNombre();
    this.path = nueva.getPath();
    this.pathInfo = nueva.getPathInfo();

    try {
      this.tipo = TipoFuente.valueOf(nueva.getTipo().toString());
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Tipo de fuente inválido: " + nueva.getTipo());
    }
  }
}

//PATH: Es la url de la API que expone la fuente.
//Ej. http://localhost:8083/api/proxy
//Ej. http://localhost:8080/api/dinamica

//PATH INFO: Es el nombre de la fuente de informacion/archivo.
//Ej. disilab
//Ej. desastres_naturales_argentina.csv

