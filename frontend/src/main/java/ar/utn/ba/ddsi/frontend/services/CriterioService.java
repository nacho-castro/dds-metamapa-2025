package ar.utn.ba.ddsi.frontend.services;

import ar.utn.ba.ddsi.frontend.dto.input.CriterioDTOInput;
import ar.utn.ba.ddsi.frontend.dto.output.CriterioDTOOutput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CriterioService {
    private final WebClient webClient;

    public CriterioService(@Value("${agregador.api.url}") String agregadorServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(agregadorServiceUrl) //base URL
                .build();
    }

    public CriterioDTOOutput crearCriterio(CriterioDTOInput dtoInput, String token){
        try{
            System.out.println("   Tipo: " + dtoInput.getTipo());
            System.out.println("   Valor1: " + dtoInput.getValor1());
            System.out.println("   Valor2: " + dtoInput.getValor2());

            CriterioDTOOutput response = webClient
                    .post()
                    .uri("/criterios")
                    .header("Authorization", "Bearer " + token) // ✅ Espacio agregado
                    .bodyValue(dtoInput)
                    .retrieve()
                    .bodyToMono(CriterioDTOOutput.class)
                    .block();

            System.out.println("✅ CriterioService: Criterio creado exitosamente");

            return response;
        }
        catch(WebClientResponseException e) {
            System.err.println("❌ Error del servidor agregador: " + e.getStatusCode());
            System.err.println("   Respuesta: " + e.getResponseBodyAsString());
            throw new RuntimeException("Error en el servicio agregador: " + e.getMessage(), e);
        }
         catch (Exception e) {
             System.err.println("❌ Error de conexión: " + e.getMessage());
            throw new RuntimeException("Error de conexión con el servicio agregador: " + e.getMessage(), e);
        }

    }
    public List<CriterioDTOOutput> obtenerCriterios(){
        try {
            System.out.println("=== LLAMANDO A API DE CRITERIOS ===");
            System.out.println("URL: " + webClient.toString() + "/criterios");
            List<CriterioDTOOutput> respuesta = webClient
                    .get()
                    .uri("/criterios")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<CriterioDTOOutput>>() {})
                    .block();

            System.out.println("=== RESPUESTA DE API CRITERIOS ===");
            System.out.println("Respuesta: " + (respuesta != null ? "OK" : "NULL"));
            System.out.println("Cantidad: " + (respuesta != null ? respuesta.size() : 0));
            return respuesta != null ? respuesta : new ArrayList<>();

        } catch (Exception e) {
            System.err.println("❌ ERROR al obtener criterios desde API: " + e.getMessage());
            return new ArrayList<>();
        }
    }

}
