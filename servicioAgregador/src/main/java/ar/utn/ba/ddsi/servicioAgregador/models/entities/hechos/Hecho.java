package ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos;

import ar.utn.ba.ddsi.servicioAgregador.config.Globals;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.FuenteAlt;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "hecho")
public class Hecho {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "titulo", nullable = false)
  private String titulo;

  @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
  private String descripcion;

  @Column(name = "categoria", nullable = false)
  private String categoria;

  @OneToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name="lugar_id", referencedColumnName = "id")
  private Lugar lugarAcontecimiento;

  @Column(name = "fecha_acontecimiento", nullable = false)
  private LocalDateTime fechaAcontecimiento;

  @Column(name = "fecha_carga", nullable = false)
  @Builder.Default
  private LocalDate fechaCarga = LocalDate.now();

  @Column(name = "activo", nullable = false)
  @Builder.Default
  private Boolean activo = true;

  @Column(name = "usuario_id", nullable = true)
  private Long usuarioId; //puede ser null (si fue anónimo/estatico)

  @ElementCollection
  @CollectionTable(name = "hecho_etiqueta", joinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id"))
  @Column(name = "etiquetas")
  private List<String> etiquetas = new ArrayList<>();

  @ElementCollection
  @CollectionTable(name = "hecho_multimedia", joinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id"))
  @Column(name = "multimedia_url")
  private List<String> multimediaUrls;

  @Column(name = "editable", nullable = false)
  private boolean editable;

  @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
  @JoinTable(
      name = "fuente_x_hecho",
      joinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "fuente_id", referencedColumnName = "id")
  )
  @Builder.Default
  private List<FuenteAlt> fuenteDeOrigen = new ArrayList<>(); //UN MISMO HECHO PERTENECE A VARIAS FUENTES

  public Hecho(){
    this.fechaCarga = LocalDate.now();
    this.usuarioId = null;
    this.activo = true;
    this.editable = false;
    this.etiquetas = new ArrayList<>();
    this.fuenteDeOrigen = new ArrayList<>();
  }

  public Hecho(String titulo, String descripcion, String categoria, Lugar lugar, LocalDateTime fecha,boolean editable, Long id, boolean esDeDinamica) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.categoria = categoria;
    this.lugarAcontecimiento = lugar;
    this.fechaAcontecimiento = fecha;
    this.fechaCarga = LocalDate.now();
    this.editable = editable;
    this.id = id;
    this.activo = true;
    this.etiquetas = new ArrayList<>();
    this.fuenteDeOrigen = new ArrayList<>();
  }

  public Hecho(String titulo, String descripcion, String categoria, Lugar lugar, LocalDateTime fecha) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.categoria = categoria;
    this.lugarAcontecimiento = lugar;
    this.fechaAcontecimiento = fecha;
    this.fechaCarga = LocalDate.now();
    this.usuarioId = null;
    this.activo = true;
    this.etiquetas = new ArrayList<>();
    this.fuenteDeOrigen = new ArrayList<>();
  }

  public void updateHecho(Hecho hechoNuevo) {
    if (hechoNuevo.getTitulo() != null) {
      this.titulo = hechoNuevo.getTitulo();
    }
    if (hechoNuevo.getDescripcion() != null) {
      this.descripcion = hechoNuevo.getDescripcion();
    }
    if (hechoNuevo.getCategoria() != null) {
      this.categoria = hechoNuevo.getCategoria();
    }
    if (hechoNuevo.getLugarAcontecimiento() != null) {
      this.lugarAcontecimiento = hechoNuevo.getLugarAcontecimiento();
    }
    if (hechoNuevo.getFechaAcontecimiento() != null) {
      this.fechaAcontecimiento = hechoNuevo.getFechaAcontecimiento();
    }
  }

  public Hecho(boolean editable){
    this.editable = editable;
  }

  //Si la persona no está registrada en la plataforma,
  //podrá subir hechos sin posibilidad de edición posterior.
  boolean puedeEditar(Long id) {
    if (this.usuarioId == null) return false;
    //En cambio, si los sube en forma registrada podrá realizar modificaciones
    //al mismo en caso de que lo necesitara, pero solo en el plazo de una semana.
    LocalDateTime ahora = LocalDateTime.now();
    return usuarioId.equals(id) && ChronoUnit.DAYS.between(fechaCarga, ahora) <= Globals.getDiasParaEdicion();
  }

  public void desactivarHecho() {
    this.activo = false;
  }

  public void setDescripcion(String descripcion) {
    if (descripcion != null && descripcion.length() > 255) {
      this.descripcion = descripcion.substring(0, 255);
    } else {
      this.descripcion = descripcion;
    }
  }

  public boolean perteneceA(FuenteAlt fuente){
    return fuenteDeOrigen.contains(fuente);
  }
}
