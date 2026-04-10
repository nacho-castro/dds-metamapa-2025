package ar.utn.ba.ddsi.fuenteEstatica;

import ar.utn.ba.ddsi.fuenteEstatica.models.entities.datasets.Dataset;
import ar.utn.ba.ddsi.fuenteEstatica.models.entities.datasets.DatasetCsv;
import ar.utn.ba.ddsi.fuenteEstatica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteEstatica.models.entities.hechos.Lugar;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class fuenteEstaticaApplicationTests {
/*
	String filename1 = "desastres_naturales_argentina.csv"; //15000
	String filename2 = "desastres_sanitarios_contaminacion_argentina.csv"; //10000
  String filename3 = "desastres_tecnologicos_argentina.csv"; //10000
	Dataset dataset1;
	Dataset dataset2;
	Dataset dataset3;
	Hecho hecho1;
	Hecho hecho2;
	Hecho hecho3;

	@BeforeEach
	public void init(){
		this.dataset1 = new DatasetCsv(filename1);
		this.dataset2 = new DatasetCsv(filename2);
		this.dataset3 = new DatasetCsv(filename3);
		this.hecho1 = new Hecho("\"Ráfagas de más de 100 km/h causa estragos en San Vicente, Misiones\"","La región de San Vicente en Misiones sufrió los efectos de una intensa ráfagas de más de 100 km/h. El incidente obligando a evacuar a residentes de la zona. Se ha convocado al comité de crisis para coordinar las acciones de respuesta.", "Ráfagas de más de 100 km/h",new Lugar(-27.029465,-54.436559), LocalDateTime.of(2007,12,21,0, 0));
		this.hecho2 = new Hecho("\"Crisis sanitaria afecta a Tilcara, Jujuy\"","\"Grave crisis sanitaria ocurrió en las inmediaciones de Tilcara, Jujuy. El incidente afectando gravemente la infraestructura local. Se ha declarado estado de emergencia en la región para facilitar la asistencia.\"","Desastre Tecnológico - Evento sanitario",new Lugar(-23.574039,-65.36991), LocalDateTime.of(2013, 11,11,0, 0));
		this.hecho3 = new Hecho("Chivilcoy en alerta por Emanación de gas tóxico","\"Grave emanación de gas tóxico ocurrió en las inmediaciones de Chivilcoy, Buenos Aires. El incidente generando caos en el transporte público y privado. Defensa Civil coordina las tareas de asistencia y reconstrucción.\"","Emanación de gas tóxico", new Lugar(-34.914536,-60.035774), LocalDateTime.of(2022, 6, 7,0, 0));
	}

	@Test
	@DisplayName("Se obtiene correctamente la informacion dataset1")
	public void obtenerInfo1(){
		List <Hecho> hechos = dataset1.obtenerInformacion();
		Assertions.assertEquals(15000, hechos.size());
	}

	@Test
	@DisplayName("Se obtiene correctamente el primer hecho de dataset1")
	public void obtenerHecho1(){
		List <Hecho> hechos = dataset1.obtenerInformacion();
		Hecho hecho = hechos.stream().findFirst().get();
		Assertions.assertEquals(hecho.getTitulo(), hecho1.getTitulo());
		Assertions.assertEquals(hecho.getDescripcion(), hecho1.getDescripcion());
		Assertions.assertEquals(hecho.getCategoria(), hecho1.getCategoria());
		Assertions.assertEquals(hecho.getLugarAcontecimiento().getLatitud(), hecho1.getLugarAcontecimiento().getLatitud());
		Assertions.assertEquals(hecho.getFechaAcontecimiento(), hecho1.getFechaAcontecimiento());
	}

	@Test
	@DisplayName("Se obtienen correctamente la informacion dataset2")
	public void obtenerInfo2(){
		List<Hecho> hechos = dataset2.obtenerInformacion();
		Assertions.assertEquals(10000,hechos.size());
	}

	@Test
	@DisplayName("Se obtiene correctamente el primer hecho de dataset1")
	public void obtenerHecho2(){
		List <Hecho> hechos = dataset2.obtenerInformacion();
		Hecho hecho = hechos.stream().findFirst().get();
		Assertions.assertEquals(hecho.getTitulo(), hecho2.getTitulo());
		Assertions.assertEquals(hecho.getDescripcion(), hecho2.getDescripcion());
		Assertions.assertEquals(hecho.getCategoria(), hecho2.getCategoria());
		Assertions.assertEquals(hecho.getLugarAcontecimiento().getLatitud(), hecho2.getLugarAcontecimiento().getLatitud());
		Assertions.assertEquals(hecho.getFechaAcontecimiento(), hecho2.getFechaAcontecimiento());
	}

	@Test
	@DisplayName("Se obtienen correctamente la informacion dataset3")
	public void obtenerInfo3(){
		List<Hecho> hechos = dataset3.obtenerInformacion();
		Assertions.assertEquals(10000,hechos.size());
	}

	@Test
	@DisplayName("Se obtiene correctamente el primer hecho de dataset1")
	public void obtenerHecho3(){
		List <Hecho> hechos = dataset3.obtenerInformacion();
		Hecho hecho = hechos.stream().findFirst().get();
		Assertions.assertEquals(hecho.getTitulo(), hecho3.getTitulo());
		Assertions.assertEquals(hecho.getDescripcion(), hecho3.getDescripcion());
		Assertions.assertEquals(hecho.getCategoria(), hecho3.getCategoria());
		Assertions.assertEquals(hecho.getLugarAcontecimiento().getLatitud(), hecho3.getLugarAcontecimiento().getLatitud());
		Assertions.assertEquals(hecho.getFechaAcontecimiento(), hecho3.getFechaAcontecimiento());
	}
*/
}
