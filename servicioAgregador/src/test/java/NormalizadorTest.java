import ar.utn.ba.ddsi.servicioAgregador.services.impl.NormalizadorService;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


final public class NormalizadorTest {

    NormalizadorService normalizadorService = new NormalizadorService();

    @Test
    public void VacioyRep() throws IOException, InterruptedException {
        final String texto1 = "Inundación en Buenos Aires el 30 de julio de 2024";
        final String texto2 = "Inundación en Buenos Aires el 30/07/2024";
        final String texto3 = "Rey";
        normalizadorService.agregarConjuntoTitulos(texto2);
        normalizadorService.agregarConjuntoTitulos(texto3);

        assertTrue(normalizadorService.estaDuplicado(texto1));
    }
    @Test
    public void noEsRep() throws IOException, InterruptedException {
        final String texto1 = "Erupcion de volcan en Marte";
        final String texto2 = "Derrumbe de edificio en la capital de Egipto";
        normalizadorService.agregarConjuntoTitulos(texto2);

        assertFalse(normalizadorService.estaDuplicado(texto1));
    }
    @Test
    public void noHayRepEnMuchasOpciones() throws IOException, InterruptedException{
        final String textoAChequear = "Inundación en Buenos Aires el 30 de julio de 2024";
        final String texto1 = "Concierto en La Plata el 5 de agosto";
        final String texto2 = "Rey de España anuncia nueva ley";
        final String texto3 = "Clases de yoga en Palermo";
        final String texto4 = "Partido de fútbol Boca vs River el domingo";
        final String texto5 = "Exposición de arte moderno en San Telmo";
        final String texto6 = "Viaje a Bariloche durante vacaciones";
        final String texto7 = "Torneo de ajedrez internacional";
        final String texto8 = "Receta de pastel de chocolate";
        final String texto9 = "Noticias sobre la NASA y el espacio";

        normalizadorService.agregarConjuntoTitulos(texto1);
        normalizadorService.agregarConjuntoTitulos(texto2);
        normalizadorService.agregarConjuntoTitulos(texto3);
        normalizadorService.agregarConjuntoTitulos(texto4);
        normalizadorService.agregarConjuntoTitulos(texto5);
        normalizadorService.agregarConjuntoTitulos(texto6);
        normalizadorService.agregarConjuntoTitulos(texto7);
        normalizadorService.agregarConjuntoTitulos(texto8);
        normalizadorService.agregarConjuntoTitulos(texto9);

        assertFalse(normalizadorService.estaDuplicado(textoAChequear));

    }

}
