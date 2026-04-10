package ar.edu.utn.frba.dds.tpa.estadisticas.service;

import ar.edu.utn.frba.dds.tpa.estadisticas.client.AgregadorClient;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.ColeccionDTO;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.HechoDTO;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.SolicitudDTO;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.statistics.CategoriaTopResponse;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.statistics.HoraTopCategoriaResponse;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.statistics.ProvinciaTopCategoriaResponse;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.statistics.ProvinciaTopResponse;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos.statistics.SolicitudesSpamResponse;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.entities.Estadistica;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.entities.Estado;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.entities.TipoEstadistica;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.repositories.EstadisticaRepository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class EstadisticasService {

  private final AgregadorClient client;
  private final EstadisticaRepository repoEstadistica;

  public EstadisticasService(AgregadorClient client, EstadisticaRepository repo) {
    this.client = client;
    this.repoEstadistica = repo;
  }

  //CALCULADORAS (5)
  //SON EJECUTADAS POR EL CRON DE ACUERDO A LA INFO. TRAIDA POR EL CLIENT (HTTP)

  //1
  public ProvinciaTopResponse calcularProvinciaTopPorColeccion(ColeccionDTO coleccionDTO) {

    Long coleccionId = coleccionDTO.getId();

    //Me quedo con hechos de la coleccion
    List<HechoDTO> hechos = coleccionDTO.getHechos();

    if (hechos.isEmpty()) {
      return null; // o excepción de negocio
    }

    Map<String, Long> cantidadPorProvincia = hechos.stream()
        .filter(h -> h.getProvincia() != null)
        .collect(Collectors.groupingBy(
            HechoDTO::getProvincia,
            Collectors.counting()
        ));

    if (cantidadPorProvincia.isEmpty()) {
      return null;
    }

    Map.Entry<String, Long> provinciaTop = cantidadPorProvincia.entrySet()
        .stream()
        .max(Map.Entry.comparingByValue())
        .orElseThrow();

    ProvinciaTopResponse response = new ProvinciaTopResponse(
        coleccionId,
        provinciaTop.getKey(),
        provinciaTop.getValue()
    );

    Estadistica e = new Estadistica();
    e.setTipo(TipoEstadistica.PROVINCIA_TOP_COLECCION);
    e.setColeccionId(coleccionId);
    e.setClave(response.getProvincia());
    e.setValor(response.getCantidadHechos());
    e.setFechaCalculo(LocalDateTime.now());

    repoEstadistica.save(e);

    return response;
  }

  //2
  public CategoriaTopResponse calcularCategoriaTopPorColeccion(ColeccionDTO coleccionDTO) {

    Long coleccionId = coleccionDTO.getId();

    //Me quedo con hechos de la coleccion
    List<HechoDTO> hechos = coleccionDTO.getHechos();

    if (hechos.isEmpty()) {
      return null; // o excepción de negocio
    }

    Map<String, Long> cantidadPorCategoria = hechos.stream()
        .filter(h -> h.getCategoria() != null)
        .collect(Collectors.groupingBy(
            HechoDTO::getCategoria,
            Collectors.counting()
        ));

    if (cantidadPorCategoria.isEmpty()) {
      return null;
    }

    Map.Entry<String, Long> categoriaTop = cantidadPorCategoria.entrySet()
        .stream()
        .max(Map.Entry.comparingByValue())
        .orElseThrow();

    CategoriaTopResponse response = new CategoriaTopResponse(
        coleccionId,
        categoriaTop.getKey(),
        categoriaTop.getValue()
    );

    Estadistica e = new Estadistica();
    e.setTipo(TipoEstadistica.CATEGORIA_TOP_COLECCION);
    e.setColeccionId(coleccionId);
    e.setClave(response.getCategoria());
    e.setValor(response.getCantidadHechos());
    e.setFechaCalculo(LocalDateTime.now());

    repoEstadistica.save(e);

    return response;
  }

  //3
  public ProvinciaTopCategoriaResponse calcularProvinciaTopPorCategoria(String categoria, List<HechoDTO> hechos) {

    Map<String, Long> cantidadPorProvincia = hechos.stream()
        .filter(h -> h.getCategoria() != null)
        .filter(h -> h.getCategoria().equalsIgnoreCase(categoria))
        .filter(h -> h.getProvincia() != null)
        .collect(Collectors.groupingBy(
            h -> h.getProvincia(),
            Collectors.counting()
        ));

    if (cantidadPorProvincia.isEmpty()) {
      return null; // o excepción de negocio
    }

    Map.Entry<String, Long> provinciaTop = cantidadPorProvincia.entrySet()
        .stream()
        .max(Map.Entry.comparingByValue())
        .orElseThrow();

    ProvinciaTopCategoriaResponse response = new ProvinciaTopCategoriaResponse(
        categoria,
        provinciaTop.getKey(),
        provinciaTop.getValue()
    );

    Estadistica e = new Estadistica();
    e.setTipo(TipoEstadistica.PROVINCIA_TOP_CATEGORIA);
    e.setCategoria(categoria);
    e.setClave(response.getProvincia());
    e.setValor(response.getCantidadHechos());
    e.setFechaCalculo(LocalDateTime.now());

    repoEstadistica.save(e);

    return response;
  }

  //4
  public HoraTopCategoriaResponse calcularHoraTopPorCategoria(String categoria, List<HechoDTO> hechos) {

    Map<Integer, Long> cantidadPorHora = hechos.stream()
        .filter(h -> h.getCategoria() != null)
        .filter(h -> h.getCategoria().equalsIgnoreCase(categoria))
        .filter(h -> h.getFechaAcontecimiento() != null)
        .collect(Collectors.groupingBy(
            h -> h.getFechaAcontecimiento().getHour(),
            Collectors.counting()
        ));

    if (cantidadPorHora.isEmpty()) {
      return null; // o excepción de negocio
    }

    Map.Entry<Integer, Long> horaTop = cantidadPorHora.entrySet()
        .stream()
        .max(Map.Entry.comparingByValue())
        .orElseThrow();

    HoraTopCategoriaResponse response = new HoraTopCategoriaResponse(
        categoria,
        horaTop.getKey(),
        horaTop.getValue()
    );

    Estadistica e = new Estadistica();
    e.setTipo(TipoEstadistica.HORA_TOP_CATEGORIA);
    e.setCategoria(categoria);
    e.setClave(response.getHora().toString());
    e.setValor(response.getCantidadHechos());
    e.setFechaCalculo(LocalDateTime.now());

    repoEstadistica.save(e);
    return response;
  }

  //5
  public SolicitudesSpamResponse calcularSolicitudesSpam() {

    List<SolicitudDTO> solicitudes = client.obtenerSolicitudesEliminacion();

    long cantidadSpam = solicitudes.stream()
        .filter(s -> s.getEstado() != null)
        .filter(s -> s.getEstado() == Estado.RECHAZADA_POR_SPAM)
        .count();

    SolicitudesSpamResponse response = new SolicitudesSpamResponse(cantidadSpam);

    Estadistica e = new Estadistica();
    e.setTipo(TipoEstadistica.SOLICITUDES_SPAM);
    e.setClave("TOTAL");
    e.setValor(response.getCantidadSolicitudesSpam());
    e.setFechaCalculo(LocalDateTime.now());

    repoEstadistica.save(e);
    return response;
  }

  //TRAE ESTADISTICAS MAS RECIENTES DE BD
  public List<Estadistica> obtenerPorTipo(String tipo) {
    TipoEstadistica tipoEnum = TipoEstadistica.valueOf(tipo);
    return repoEstadistica.findUltimasPorTipo(tipoEnum);
  }

  //RECALCULA ESTADISTICAS.
  //POR PERFORMANCE, SE BUSCA 1 SOLA CONSULTA HTTP QUE TRAIGA JUNTOS COLECCIONES + HECHOS
  public void recalcularTodas() {
    List<ColeccionDTO> colecciones = client.obtenerColecciones(); //CONSULTA

    List<HechoDTO> hechos = colecciones.stream()
        .filter(c -> c.getHechos() != null)
        .flatMap(c -> c.getHechos().stream())
        .toList();

    // Obtener TOP 3 categorías más repetidas
    List<String> top3Categorias = hechos.stream()
        .map(HechoDTO::getCategoria)
        .filter(Objects::nonNull)
        .collect(Collectors.groupingBy(
            Function.identity(),
            Collectors.counting()
        ))
        .entrySet()
        .stream()
        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
        .limit(3)
        .map(Map.Entry::getKey)
        .toList();

    colecciones.forEach(c -> {
      calcularProvinciaTopPorColeccion(c);
      calcularCategoriaTopPorColeccion(c);
    });

    top3Categorias.forEach(cat -> {
      calcularProvinciaTopPorCategoria(cat, hechos);
      calcularHoraTopPorCategoria(cat, hechos);
    });

    calcularSolicitudesSpam();
  }

  //EXPORTAR A CSV
  public byte[] exportarCsvPorTipo(String tipo) {
    TipoEstadistica tipoEnum = TipoEstadistica.valueOf(tipo);
    List<Estadistica> datos = repoEstadistica.findByTipo(tipoEnum);

    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    StringBuilder sb = new StringBuilder(
        "tipo,coleccion_id,categoria,clave,valor,fecha_calculo\n"
    );

    datos.forEach(e ->
        sb.append(e.getTipo()).append(",")
            .append(e.getColeccionId() != null ? e.getColeccionId() : "").append(",")
            .append(e.getCategoria() != null ? e.getCategoria() : "").append(",")
            .append(e.getClave()).append(",")
            .append(e.getValor()).append(",")
            .append(e.getFechaCalculo().format(formatter))
            .append("\n")
    );

    return sb.toString().getBytes(StandardCharsets.UTF_8);
  }

}
