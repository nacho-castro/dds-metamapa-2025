package ar.utn.ba.ddsi.servicioAgregador.services.impl;

import ar.utn.ba.ddsi.servicioAgregador.services.INormalizadorService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.Normalizer;
import java.util.*;

@Service
public final class NormalizadorService implements INormalizadorService {
    private final List<String> conjuntoTitulos = new ArrayList<>();

    @Value("${huggingface.api.token}")
    private String apiKey;

    public void agregarConjuntoTitulos(final String textoAAgregar){
        conjuntoTitulos.add(textoAAgregar);
    }

    public String mapear(String textoAMapear){
        textoAMapear = textoAMapear.toLowerCase().strip();
        textoAMapear = Normalizer.normalize(textoAMapear, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        Map<String, String> diccionario = Map.of(
                "bs as", "buenos aires",
                "caba", "ciudad de buenos aires"
        );

        for (Map.Entry<String, String> entry : diccionario.entrySet()) {
            textoAMapear = textoAMapear.replace(entry.getKey(), entry.getValue());
        }

        textoAMapear = textoAMapear.replaceAll("\\s+", " ").strip();

        return textoAMapear;
    }

    public boolean estaDuplicado(final String texto) throws IOException, InterruptedException {
        final String textoAVerificar = mapear(texto);

        if(conjuntoTitulos.isEmpty()) {
            conjuntoTitulos.add(textoAVerificar);
            return false; //se agrego y listo
        }
        if (conjuntoTitulos.contains(textoAVerificar)) {
            System.out.println("Duplicado exacto encontrado: " + textoAVerificar);
            return true;
        }

        //tenemos que convertir a json nuestro input para mandar a la API
        Map<String, Object> inputs = new HashMap<>();
        inputs.put("source_sentence", textoAVerificar);
        inputs.put("sentences", conjuntoTitulos);

        Map<String, Object> payload = new HashMap<>();
        payload.put("inputs", inputs);

        ObjectMapper mapper = new ObjectMapper();
        String requestBody = mapper.writeValueAsString(payload);
        List<Double> conjuntoFinal = hacerRequest(requestBody);

        boolean esDuplicado = tieneSimilitudAlta(conjuntoFinal);

        if (esDuplicado) {
            System.out.println("Duplicado semántico encontrado para: " + textoAVerificar);
            System.out.println("Similitudes: " + conjuntoFinal);
        }

        return esDuplicado;
    }

    public List<Double> hacerRequest(final String mensaje) throws IOException, InterruptedException {
      final ObjectMapper mapper = new ObjectMapper();
      final HttpClient cliente = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create("https://router.huggingface.co/hf-inference/models/sentence-transformers/all-MiniLM-L6-v2"))
          .header("Authorization", "Bearer " + apiKey)
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(mensaje))
          .build();

      HttpResponse<String> respuesta = cliente.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Respuesta cruda: " + respuesta.body());

        if (respuesta.statusCode() != 200) {
            System.out.println("Error en la respuesta: " + respuesta.statusCode());
            System.out.println(respuesta.body());
            return Collections.emptyList();
        }

      return mapper.readValue(respuesta.body(), new TypeReference<List<Double>>() {});
    }

    public boolean tieneSimilitudAlta(List<Double> similitudes) {
        if (similitudes == null || similitudes.isEmpty()) {
            return false;
        }
        for (Double similitud : similitudes) {
            if (similitud >= 0.7) {
                return true; // Sí está duplicado
            }
        }
        return false; // No está duplicado
    }
}
