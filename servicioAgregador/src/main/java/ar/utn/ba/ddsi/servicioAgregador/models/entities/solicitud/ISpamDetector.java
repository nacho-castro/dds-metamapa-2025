package ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud;

public interface ISpamDetector {
  public boolean esSpam(String mensaje);
}
