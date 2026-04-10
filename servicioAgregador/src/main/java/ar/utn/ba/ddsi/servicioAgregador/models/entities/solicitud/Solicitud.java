package ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud;


import ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.Coleccion;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "solicitud")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="tipo")
public abstract class Solicitud {
  // Cualquier hecho del sistema debe admitir solicitudes de eliminación,
  // para aquellas situaciones adecuadamente fundadas
  // (por ahora, mediante un texto de al menos 500 caracteres)
  // en que se deba eliminar del sitio la información, aún cuando esté en una fuente
  //PODRIA SER COMMAND?

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private LocalDateTime fecha;

  @ManyToOne(optional = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "coleccion_id", nullable = true)
  private Coleccion coleccion;

  @ManyToOne(optional = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "hecho_id", nullable = true)
  private Hecho hecho;

  @OneToMany(mappedBy = "solicitud", fetch = FetchType.LAZY,
      cascade = CascadeType.ALL, orphanRemoval = true)
  private List<EstadoBis> historialEstados = new ArrayList<>(); //trazabilidad

  @Enumerated(EnumType.STRING)
  @Column(name = "estado_actual")
  private Estado estadoActual;

  public Estado getEstadoActualidad() {
    return this.estadoActual;
  }

  //CAMBIAR ESTADO DE LA SOLICITUD
  public void agregarNuevoEstado(Estado nuevoEstado) {
    EstadoBis nuevo = new EstadoBis();
    nuevo.setEstadoActual(nuevoEstado);
    nuevo.setFechaCambio(LocalDateTime.now());
    nuevo.setSolicitud(this);
    historialEstados.add(nuevo);
    this.estadoActual = nuevoEstado;
  }

  public void cambiarEstado(Estado nuevoEstado) {
    if (historialEstados.isEmpty()) {
      throw new IllegalStateException("No se puede cambiar el estado porque no hay historial de estados.");
    }
    agregarNuevoEstado(nuevoEstado);
  }
}