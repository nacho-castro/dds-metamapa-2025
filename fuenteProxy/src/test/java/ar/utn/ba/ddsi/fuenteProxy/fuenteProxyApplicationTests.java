package ar.utn.ba.ddsi.fuenteProxy;

import ar.utn.ba.ddsi.fuenteProxy.models.dtos.input.external.api.ApiHechoDTO;

import java.util.List;

import ar.utn.ba.ddsi.fuenteProxy.models.dtos.input.external.disilab.DisilabHechoDTO;
import ar.utn.ba.ddsi.fuenteProxy.models.dtos.output.HechoDTOOutput;
import ar.utn.ba.ddsi.fuenteProxy.services.impl.DisilabAdapter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class fuenteProxyApplicationTests {

  @Autowired
  DisilabAdapter disilabService;

  @Test
  @DisplayName("Se obtiene el token correctamente")
  public void apiToken() {
    String token = disilabService.obtenerToken();
    System.out.println("Token: " + token);
    Assertions.assertNotNull(token);
    Assertions.assertFalse(token.isBlank());
  }

  @Test
  @DisplayName("Se obtienen los hechos correctamente de pag1")
  public void getHechos() {
    List<HechoDTOOutput> hechos = disilabService.obtenerHechos();
    Assertions.assertNotNull(hechos);
    Assertions.assertFalse(hechos.isEmpty());
    System.out.println("Primer hecho: " + hechos.get(0).getTitulo());
  }

  @Test
  @DisplayName("Se obtiene un hecho por ID correctamente")
  public void getHechoById() {
    Long id = 1L;
    HechoDTOOutput hecho = disilabService.obtenerHechoPorId(id);

    Assertions.assertNotNull(hecho, "El hecho no debe ser nulo");
    Assertions.assertNotNull(hecho.getTitulo(), "El título del hecho no debe ser nulo");
    System.out.println("Hecho obtenido: " + hecho.getTitulo());
  }
}
