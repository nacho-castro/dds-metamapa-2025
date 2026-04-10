package ar.utn.ba.ddsi.servicioAgregador.schedulers;

import ar.utn.ba.ddsi.servicioAgregador.services.IColeccionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ColeccionesScheduler {

  private final IColeccionService coleccionService;

  public ColeccionesScheduler(IColeccionService coleccionService) {
    this.coleccionService = coleccionService;
  }

  // Se ejecuta cada 30 minutos
  @Scheduled(fixedRate = 30 * 60 * 1000)
  public void refrescoProgramado() {
      try {
          coleccionService.refrescarColecciones();
      } catch (java.io.IOException e) {
          throw new RuntimeException(e);
      } catch (InterruptedException e) {
          throw new RuntimeException(e);
      }
  }
}
