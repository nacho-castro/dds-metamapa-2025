package ar.utn.ba.ddsi.fuenteDinamica;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class fuenteDinamicaApplicationTests {
  /*HechoDTOInput dto1;
  HechoDTOInput dto2;
  IUsuarioRepository usuarioRepository;
  IHechoRepository hechoRepository;
  Usuario pp;
  Usuario sofi;
  Usuario nacho;
  UsuarioDaoMemory usuarioDao;
  HechoDaoMemory hechoDaoMemory;
  FuenteDinamicaService fuenteDinamicaService;
  HechoService hechoService;
  UsuarioService usuarioService;

  @BeforeEach
  void init() {
		// Instanciar memoria compartida
		this.usuarioDao = new UsuarioDaoMemory();
		this.hechoDaoMemory = new HechoDaoMemory();
		this.fuenteDinamicaService = new FuenteDinamicaService(hechoService, usuarioService);

		// Crear usuarios
		this.sofi = new Usuario("sofi@mail.com","Sofi", "princesasof", 20, new Rol(TipoRoles.CONTRIBUYENTE, null));
		this.pp = new Usuario("pepe@mail.com","Pepe", "reyminecraft", 20, new Rol(TipoRoles.ADMINISTRADOR, null));
		this.nacho = new Usuario("nacho@mail.com","nacho", "linustorvald", 20, new Rol(TipoRoles.VISUALIZADOR, null));

		// Guardar usuarios en memoria
		usuarioRepository.save(sofi);
		usuarioRepository.save(pp);
		usuarioRepository.save(nacho);

    //HECHOS
    this.dto1 = new HechoDTOInput("NuevoTitulo", "descNuevo", "cat", LocalDate.of(2022, 7, 7), new LugarDTOOutput(2.1, 2.4));
    this.dto2 = new HechoDTOInput("OtroTitulo", "descOtro", "cat", LocalDate.of(2025, 6, 6), new LugarDTOOutput(2.1, 2.4));
  }

  @Test
  @DisplayName("Generación de ID's correcta")
  void generacionID() {
    Assertions.assertEquals(sofi, usuarioRepository.findById(1L));
    Assertions.assertEquals(pp, usuarioRepository.findById(2L));
    Assertions.assertEquals(nacho, usuarioRepository.findById(3L));
  }

	@Test
	@DisplayName("Un usuario sube un hecho")
	void usuarioSubeHecho() {
		Long idSofi = sofi.getId();
		fuenteDinamicaService.subirHecho(dto1, idSofi);

		Hecho hecho = hechoRepository.findById(1L);
		Assertions.assertEquals(dto1.getTitulo(), hecho.getTitulo());
		Assertions.assertNotNull(hecho.getCreador(), "El hecho no tiene creador asignado");
		Assertions.assertEquals("Sofi", hecho.getCreador().getNombre());
	}

  @Test
  @DisplayName("Se editan hechos correctamente")
  void editarHecho() {
    String tituloOriginal = dto1.getTitulo();
    fuenteDinamicaService.subirHecho(dto1, 3L);

		fuenteDinamicaService.editarHecho(1L,dto2);
    Assertions.assertEquals(dto2.getTitulo(), hechoRepository.findById(1L).getTitulo());
    Assertions.assertNotEquals(tituloOriginal, hechoRepository.findById(1L).getTitulo());
  }

  @Test
  @DisplayName("Se listan los hechos correctamente")
  void listarHechos() {
    fuenteDinamicaService.subirHecho(dto1, 3L);
    fuenteDinamicaService.subirHecho(dto2, 3L);
    List<HechoDTOOutput> hechos = fuenteDinamicaService.obtenerHechos();
    Assertions.assertEquals(2, hechos.size());
    Assertions.assertEquals("NuevoTitulo", hechoRepository.findById(1L).getTitulo());
  }

  @Test
  @DisplayName("Se eliminan los hechos correctamente")
  void eliminarHechos() {
    fuenteDinamicaService.subirHecho(dto1, 3L);
    fuenteDinamicaService.subirHecho(dto2, 3L);
    List<HechoDTOOutput> hechos = fuenteDinamicaService.obtenerHechos();
    Assertions.assertEquals(2, hechos.size());

		fuenteDinamicaService.eliminarHecho(2L);
		hechos = fuenteDinamicaService.obtenerHechos();

		Assertions.assertEquals(1, hechos.size());
    Assertions.assertEquals("NuevoTitulo", hechoRepository.findById(1L).getTitulo());
  }*/

}
