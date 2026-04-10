package ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud;

import java.util.Arrays;
import java.util.List;

public class SpamDetector implements ISpamDetector {

  private static final List<String> PALABRAS_SPAM = Arrays.asList(
      "gratis", "gana dinero", "haz clic aquí", "oferta limitada", "bitcoin", "trabajo desde casa"
  );

  @Override
  public boolean esSpam(String mensaje) {
    String normalizado = mensaje.toLowerCase();

    return PALABRAS_SPAM.stream().anyMatch(normalizado::contains);
  }
}

