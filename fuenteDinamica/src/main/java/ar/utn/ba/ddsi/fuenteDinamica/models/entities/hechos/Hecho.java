package ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name="hecho")
public class Hecho {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "titulo", nullable = false)
  private String titulo;

  @Column(name = "descripcion", nullable = false)
  private String descripcion;

  @Column(name = "categoria", nullable = false)
  private String categoria;

  //@OneToOne
  @OneToOne(cascade = CascadeType.PERSIST)
  //SI ES LUGAR NO EXISTE, LO PERSISTE
  //PUEDEN REPETIRSE LUGARES. NO USO ID
  @JoinColumn(name="lugar_id", referencedColumnName = "id")
  private Lugar lugarAcontecimiento;

  @Column(name = "fecha_acontecimiento", nullable = false)
  private LocalDateTime fechaAcontecimiento;

  @Column(name = "fecha_carga", nullable = false)
  private LocalDateTime fechaCarga;

  @Column(name = "activo", nullable = false)
  private Boolean activo;

  //FK hacia Usuario, puede ser null
  @Column(name = "usuario_id")
  private Long usuarioId;  // puede ser null -> anónimo

  @ElementCollection
  @CollectionTable(name = "hecho_etiqueta", joinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id"))
  @Column(name = "etiqueta")
  private List<String> etiquetas;


  @ElementCollection
  @CollectionTable(name = "hecho_multimedia", joinColumns = @JoinColumn(name = "hecho_id", referencedColumnName = "id"))
  @Column(name = "multimedia_url")
  private List<String> multimediaUrls;

  @Column(name = "editable", nullable = false)
  private boolean editable;

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

}
