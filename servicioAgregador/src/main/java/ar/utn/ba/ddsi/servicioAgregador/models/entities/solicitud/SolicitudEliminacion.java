package ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@DiscriminatorValue("eliminacion")
public class SolicitudEliminacion extends Solicitud {

  @Column(name="minCaracter")
  private int minCaracter = 500;

  @Column(name="motivoBorrado")
  private String motivoBorrado; // se explica en hasta 500 caracteres porque borrarlo

  public SolicitudEliminacion() {
    super();
    agregarNuevoEstado(Estado.PENDIENTE); // o el estado que quieras por defecto
  }
}
