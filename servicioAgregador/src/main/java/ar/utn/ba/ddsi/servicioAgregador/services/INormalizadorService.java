package ar.utn.ba.ddsi.servicioAgregador.services;

import java.io.IOException;
import java.util.List;

public interface INormalizadorService {
  void agregarConjuntoTitulos(final String textoAAgregar);
  String mapear(String textoAMapear);
  boolean estaDuplicado(final String texto) throws IOException, InterruptedException;
  List<Double> hacerRequest(final String mensaje) throws IOException, InterruptedException;
  boolean tieneSimilitudAlta(List<Double> conjunto);
}
