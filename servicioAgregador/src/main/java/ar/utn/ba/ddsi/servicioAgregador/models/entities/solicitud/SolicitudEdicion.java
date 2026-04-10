package ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("edicion") // Esto diferencia el tipo en la tabla padre (si usas SINGLE_TABLE o JOINED)
public class SolicitudEdicion extends Solicitud {

  // Guardamos los valores NUEVOS que el usuario propone
  // Si apruebas la solicitud, copiarás estos valores al Hecho original.

  @Column(name = "titulo_propuesto")
  private String tituloPropuesto;

  @Column(name = "descripcion_propuesta", length = 2000) // Un poco más largo por si acaso
  private String descripcionPropuesta;

  @Column(name = "categoria_propuesta")
  private String categoriaPropuesta;

  @Column(name = "motivo_edicion")
  private String motivo; // La razón del cambio (ej: "Ortografía")

  // Constructor vacío requerido por JPA
  public SolicitudEdicion() {
    super();
    // Al crearla, nace PENDIENTE
    this.agregarNuevoEstado(Estado.PENDIENTE);
  }

  // Constructor utilitario (opcional, pero cómodo)
  public SolicitudEdicion(String tituloPropuesto, String descripcionPropuesta, String categoriaPropuesta, String motivo) {
    super();
    this.tituloPropuesto = tituloPropuesto;
    this.descripcionPropuesta = descripcionPropuesta;
    this.categoriaPropuesta = categoriaPropuesta;
    this.motivo = motivo;
    this.agregarNuevoEstado(Estado.PENDIENTE);
  }
}
