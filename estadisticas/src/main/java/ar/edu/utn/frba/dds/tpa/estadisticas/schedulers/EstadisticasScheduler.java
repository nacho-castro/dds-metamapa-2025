package ar.edu.utn.frba.dds.tpa.estadisticas.schedulers;

import ar.edu.utn.frba.dds.tpa.estadisticas.client.AgregadorClient;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.ColeccionDTO;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.HechoDTO;
import ar.edu.utn.frba.dds.tpa.estadisticas.service.EstadisticasService;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EstadisticasScheduler {

  private static final Logger log = LoggerFactory.getLogger(EstadisticasScheduler.class);

  private final EstadisticasService estadisticasService;
  private final AgregadorClient agregadorClient;

  public EstadisticasScheduler(
      EstadisticasService estadisticasService,
      AgregadorClient agregadorClient
  ) {
    this.estadisticasService = estadisticasService;
    this.agregadorClient = agregadorClient;
  }

  @PostConstruct
  public void ejecutarAlIniciar() {
    log.info("Ejecución inicial de estadísticas al iniciar la aplicación");
    ejecutarDiario();
  }

  // Se ejecuta diariamente a las 00:00
  @Scheduled(cron = "0 0 0 * * ?")
  public void ejecutarDiario() {

    log.info("Iniciando recálculo de estadísticas");

    estadisticasService.recalcularTodas();

    log.info("Recálculo de estadísticas finalizado");
  }
}
