package ar.utn.ba.ddsi.fuenteEstatica.models.entities.datasets;

import ar.utn.ba.ddsi.fuenteEstatica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteEstatica.models.entities.hechos.Lugar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ImportadorCSV {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public static Hecho parsearLinea(String linea) {
    try {
      String[] listaDeValores = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
      if (listaDeValores.length < 6) {
        return null;
      }

      String titulo = listaDeValores[0];
      String descripcion = listaDeValores[1];
      String categoria = listaDeValores[2];
      double latitud = Double.parseDouble(listaDeValores[3]);
      double longitud = Double.parseDouble(listaDeValores[4]);

      // Parseamos la fecha como LocalDate
      LocalDate fechaSolo = LocalDate.parse(listaDeValores[5], formatter);
      // Convertimos a LocalDateTime con hora 00:00
      LocalDateTime fecha = fechaSolo.atStartOfDay();

      return new Hecho(titulo,descripcion,categoria,new Lugar(latitud,longitud),fecha);
    } catch (Exception e) {
      // Línea inválida → la ignoramos devolviendo null
      return null;
    }
  }
}
