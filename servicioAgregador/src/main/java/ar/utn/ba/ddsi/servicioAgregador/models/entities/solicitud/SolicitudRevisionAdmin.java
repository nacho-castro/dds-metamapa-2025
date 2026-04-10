package ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("revisionAdmin")
public class SolicitudRevisionAdmin extends Solicitud{
  //todo
}
