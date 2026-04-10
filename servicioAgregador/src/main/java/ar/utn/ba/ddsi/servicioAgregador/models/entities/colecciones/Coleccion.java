package ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.algoritmosConsenso.TiposAlgoritmos;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.criterios.Criterio;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.FuenteAlt;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coleccion")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Coleccion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="titulo")
  private String titulo;

  @Column(name = "descripcion")
  private String descripcion;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_algoritmo")
  private TiposAlgoritmos algoritmoConsenso;

  @ManyToMany
  @JoinTable(
      name = "criterio_x_coleccion",
      joinColumns = @JoinColumn(name = "coleccion_id"),
      inverseJoinColumns = @JoinColumn(name = "criterio_id")
  )
  private List<Criterio> criterios;

  @ManyToMany
  @JoinTable(
      name = "hecho_x_coleccion",
      joinColumns = @JoinColumn(name = "coleccion_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id")
  )
  private List<Hecho> hechos = new ArrayList<>();

  //Si la fuente no existe, no se permite persistir
  @ManyToMany(cascade = {CascadeType.MERGE})
  @JoinTable(
      name = "fuente_x_coleccion",
      joinColumns = @JoinColumn(name = "coleccion_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "fuente_id", referencedColumnName = "id")
  )
  protected List<FuenteAlt> fuentes;

  public Coleccion(String titulo, String descripcion) {
    this.titulo = titulo;
    this.descripcion = descripcion;
  }

  public void update(Coleccion nueva) {
    if (nueva.getTitulo() != null) {
      this.titulo = nueva.getTitulo();
    }
    if (nueva.getDescripcion() != null) {
      this.descripcion = nueva.getDescripcion();
    }
  }

  public void agregarFuente(FuenteAlt fuente){
    this.fuentes.add(fuente);
  }

  public void eliminarFuente(FuenteAlt fuente){
    this.fuentes.remove(fuente);
  }
}
//conjuntos de hechos organizados bajo un título y descripción,
//creados y gestionados por administradores.
//Son públicas y no pueden ser editadas ni eliminadas manualmente.
//Debo poder agregar y quitarle fuentes