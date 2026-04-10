package ar.edu.utn.frba.dds.tpa.estadisticas.controller;

import ar.edu.utn.frba.dds.tpa.estadisticas.models.entities.Estadistica;
import ar.edu.utn.frba.dds.tpa.estadisticas.service.EstadisticasService;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estadisticas")
public class EstadisticasController {

  private final EstadisticasService service;

  public EstadisticasController(EstadisticasService service) {
    this.service = service;
  }

  //PUEDE SER ESTADISTICA POR COLECCION O CATEGORIA
  @GetMapping("/{tipo}")
  public ResponseEntity<?> getByTipo(
      @PathVariable("tipo") String tipo
  ) {
    return ResponseEntity.ok(
        service.obtenerPorTipo(tipo)
    );
  }

  @GetMapping(value = "/export/{tipo}", produces = "text/csv")
  public ResponseEntity<ByteArrayResource> export(@PathVariable("tipo") String tipo) {
    byte[] csv = service.exportarCsvPorTipo(tipo);

    ByteArrayResource resource = new ByteArrayResource(csv);

    return ResponseEntity.ok()
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=" + tipo + ".csv"
        )
        .contentType(MediaType.parseMediaType("text/csv"))
        .body(resource);
  }
}
