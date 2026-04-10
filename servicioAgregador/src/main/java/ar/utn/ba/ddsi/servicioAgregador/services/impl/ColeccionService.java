package ar.utn.ba.ddsi.servicioAgregador.services.impl;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.ColeccionDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.EditColeccionDTO;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.ColeccionDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.HechoDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.PaginacionResponseDTO;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.algoritmosConsenso.AlgoritmoFactory;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.algoritmosConsenso.IAlgoritmosConsenso;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.algoritmosConsenso.TiposAlgoritmos;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.Coleccion;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.ColeccionMapper;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.criterios.Criterio;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.FuenteAlt;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.HechoMapper;
import ar.utn.ba.ddsi.servicioAgregador.models.repositories.IColeccionRepository;
import ar.utn.ba.ddsi.servicioAgregador.models.repositories.ICriterioRepository;
import ar.utn.ba.ddsi.servicioAgregador.models.repositories.IFuenteRepository;
import ar.utn.ba.ddsi.servicioAgregador.models.repositories.IHechoRepository;
import ar.utn.ba.ddsi.servicioAgregador.services.IColeccionService;
import ar.utn.ba.ddsi.servicioAgregador.services.IFuenteDinamicaService;
import ar.utn.ba.ddsi.servicioAgregador.services.IFuenteEstaticaService;
import ar.utn.ba.ddsi.servicioAgregador.services.IFuenteProxyService;
import ar.utn.ba.ddsi.servicioAgregador.services.INormalizadorService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class ColeccionService implements IColeccionService {

  private static final Logger logger = LoggerFactory.getLogger(ColeccionService.class);
  private IColeccionRepository coleccionRepository;
  private IFuenteDinamicaService dinamicaService;
  private IFuenteEstaticaService estaticaService;
  private IFuenteProxyService proxyService;
  private IHechoRepository hechoRepository;
  private IFuenteRepository fuenteRepository;
  private ICriterioRepository criterioRepository;

  @Autowired
  private INormalizadorService normalizadorService;

  public ColeccionService(IColeccionRepository coleccionRepository, IFuenteDinamicaService dinamicaService, IFuenteEstaticaService estaticaService, IFuenteProxyService proxyService, IHechoRepository hechoRepository, IFuenteRepository fuenteRepository, ICriterioRepository criterioRepository) {
    this.coleccionRepository = coleccionRepository;
    this.dinamicaService = dinamicaService;
    this.estaticaService = estaticaService;
    this.proxyService = proxyService;
    this.hechoRepository = hechoRepository;
    this.fuenteRepository = fuenteRepository;
    this.criterioRepository = criterioRepository;
    this.criterioRepository = criterioRepository;
  }

  //============= CRUD COLECCION =========

  @Override
  public ColeccionDTOOutput guardarColeccion(ColeccionDTOInput dtoInput) {

    // --- PROTECCIÓN: Si llegan nulos, usamos listas vacías para evitar el error ---
    List<Long> idsFuentes = dtoInput.getFuentes() != null ? dtoInput.getFuentes() : new ArrayList<>();
    List<Long> idsCriterios = dtoInput.getCriterios() != null ? dtoInput.getCriterios() : new ArrayList<>();

    // 1) Buscar fuentes (Solo llamamos a la BD si hay IDs)
    List<FuenteAlt> fuentes = new ArrayList<>();
    if (!idsFuentes.isEmpty()) {
      fuentes = fuenteRepository.findAllById(idsFuentes);
      if (fuentes.size() != idsFuentes.size()) {
        throw new RuntimeException("Una o más fuentes no existen");
      }
    }

    // 2) Buscar criterios (Solo llamamos a la BD si hay IDs)
    List<Criterio> criterios = new ArrayList<>();
    if (!idsCriterios.isEmpty()) {
      criterios = criterioRepository.findAllById(idsCriterios);
      if (criterios.size() != idsCriterios.size()) {
        throw new RuntimeException("Uno o más criterios no existen");
      }
    }

    // 3) Crear colección
    Coleccion coleccionNueva = ColeccionMapper.dtoToColeccion(dtoInput);
    coleccionNueva.setFuentes(fuentes);
    coleccionNueva.setCriterios(criterios);

    // 4) Traer hechos de fuentes desde BD
    // OPTIMIZACIÓN: Si no hay fuentes, no gastamos tiempo consultando a la BD
    List<Hecho> hechosDeFuentes = new ArrayList<>();

    if (!fuentes.isEmpty()) {
      List<Long> fuenteIds = fuentes.stream()
          .map(FuenteAlt::getId)
          .toList();

      // OJO: Aquí es donde tenías el problema de rendimiento (N+1 queries).
      // Si puedes, asegúrate que este método traiga los datos relacionados o usa @EntityGraph
      hechosDeFuentes = hechoRepository.findDistinctByFuenteDeOrigen_IdIn(fuenteIds);
    }

    // 5) FILTRAR HECHOS SEGÚN CRITERIOS
    // Nota: Si no hay criterios, 'allMatch' devuelve true, así que pasan todos los hechos (comportamiento correcto).
    List<Criterio> finalCriterios = criterios;
    List<Hecho> hechosQueCumplen = hechosDeFuentes.stream()
        .filter(h -> finalCriterios.stream().allMatch(c -> c.cumpleCriterio(h)))
        .toList();

    // 6) GUARDAR RELACIÓN COLECCIÓN <-> HECHOS
    coleccionNueva.setHechos(hechosQueCumplen);
    coleccionRepository.save(coleccionNueva);

    return ColeccionMapper.coleccionToDTO(coleccionNueva);
  }

  //READ (DTO) paginado
  @Override
  public PaginacionResponseDTO<ColeccionDTOOutput> obtenerColecciones(int page, int limit, String titulo) {
    Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(limit, 1));

    Page<Coleccion> pageResult;

    if (titulo != null && !titulo.isBlank()) {
      pageResult = coleccionRepository.findByTituloContainingIgnoreCase(titulo, pageable);
    } else {
      pageResult = coleccionRepository.findAll(pageable);
    }

    List<ColeccionDTOOutput> content = pageResult
        .getContent()
        .stream()
        .map(coleccion -> ColeccionMapper.coleccionToDTO(coleccion))
        .toList();

    return new PaginacionResponseDTO<>(
        content,
        pageResult.getNumber(),
        pageResult.getSize(),
        pageResult.getTotalElements(),
        pageResult.getTotalPages()
    );
  }

  //FIND BY ID
  @Override
  public ColeccionDTOOutput buscarColeccion(Long idColeccion) {
    Coleccion coleccion = coleccionRepository.findById(idColeccion)
        .orElseThrow(() -> new RuntimeException("Coleccion no encontrada con id: " + idColeccion));
    return ColeccionMapper.coleccionToDTO(coleccion);
  }

  //UPDATE
  @Override
  public ColeccionDTOOutput actualizarColeccion(Long idColeccion, EditColeccionDTO nueva) {
    Coleccion coleccion = coleccionRepository.findById(idColeccion)
        .orElseThrow(() -> new RuntimeException("Coleccion no encontrada con id: " + idColeccion));
    Coleccion coleccionNueva = new Coleccion(nueva.getTitulo(), nueva.getDescripcion());
    coleccion.update(coleccionNueva);
    coleccionRepository.save(coleccion);
    return ColeccionMapper.coleccionToDTO(coleccion);
  }

  //DELETE
  @Override
  public ColeccionDTOOutput borrarColeccion(Long idColeccion) {
    Coleccion coleccion = coleccionRepository.findById(idColeccion)
        .orElseThrow(() -> new RuntimeException("Coleccion no encontrada con id: " + idColeccion));
    coleccionRepository.delete(coleccion);
    return ColeccionMapper.coleccionToDTO(coleccion);
  }

  //GET HECHOS DE LA COLECCION
  @Override
  public List<HechoDTOOutput> obtenerHechos(Long idColeccion, boolean curada) {
    Coleccion coleccion = coleccionRepository.findById(idColeccion)
        .orElseThrow(() -> new RuntimeException("Coleccion no encontrada con id: " + idColeccion));

    // Obtener la lista de hechos persistidos para comparar
    List<Hecho> hechosColeccion = coleccion.getHechos();

    // Lista de hechos a filtrar y modificar
    List<Hecho> hechosCurados = hechosColeccion;

    /*
    List<Hecho> hechosFiltrados = hechosCurados.stream()
        .filter(hecho -> coleccion.getCriterios().stream()
            .allMatch(criterio -> criterio.cumpleCriterio(hecho)))
        .collect(Collectors.toList());
     */

    if (curada) {
      // Obtener tipo de algoritmo de la colección
      TiposAlgoritmos tipoAlgoritmo = coleccion.getAlgoritmoConsenso();

      // Crear el algoritmo concreto usando Factory
      List<FuenteAlt> todasLasFuentes = fuenteRepository.findAll();
      IAlgoritmosConsenso algoritmo = AlgoritmoFactory.crearAlgoritmo(tipoAlgoritmo, todasLasFuentes);

      // Filtrar hechos según el algoritmo
      hechosCurados = hechosCurados.stream()
          .filter(h -> algoritmo.estaConsensuado(h, hechosColeccion))
          .toList();
    }

    return HechoMapper.hechoToDTO(hechosCurados);
  }

  //GET HECHOS DE LA COLECCION
  @Override
  public PaginacionResponseDTO<HechoDTOOutput> obtenerHechosPaginados(
      int page, int limit, Long idColeccion, boolean curada,
      String keyword, String categoria, LocalDate fechaInicio, LocalDate fechaFin) {

    // 1. Configurar paginación (solo para calcular offsets si fuera JPA directo, aquí es manual)
    // Se usa Math.max para evitar índices negativos
    Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(limit, 1));

    // 2. Buscar la colección
    Coleccion coleccion = coleccionRepository.findById(idColeccion)
        .orElseThrow(() -> new RuntimeException("Coleccion no encontrada con id: " + idColeccion));

    // 3. Obtener hechos y FILTRAR LOS INACTIVOS (Soft Delete)
    // Esto es lo nuevo: solo pasan los que tienen activo = true
    List<Hecho> hechosActivos = coleccion.getHechos().stream()
        .filter(h -> Boolean.TRUE.equals(h.getActivo()))
        .toList();

    // 4. Lógica de Curación (Algoritmos de Consenso)
    // Trabajamos sobre la lista 'hechosActivos' limpia
    List<Hecho> hechosParaFiltrar = hechosActivos;

    if (curada && coleccion.getAlgoritmoConsenso() != TiposAlgoritmos.NOHAYALGORITMO) {
      // Obtenemos las fuentes configuradas en la colección
      List<FuenteAlt> fuentesColeccion = coleccion.getFuentes();

      // Creamos la instancia del algoritmo
      IAlgoritmosConsenso algoritmo = AlgoritmoFactory.crearAlgoritmo(coleccion.getAlgoritmoConsenso(), fuentesColeccion);

      // Filtramos: Solo pasan los hechos que el algoritmo aprueba
      hechosParaFiltrar = hechosParaFiltrar.stream()
          .filter(h -> algoritmo.estaConsensuado(h, hechosActivos))
          .toList();
    }

    // 5. Aplicar Filtros de Búsqueda (Keyword, Categoría, Fechas)
    Stream<Hecho> streamFiltrado = hechosParaFiltrar.stream();

    // Filtro: Palabra Clave (busca en Título O Descripción)
    if (keyword != null && !keyword.isBlank()) {
      String kw = keyword.toLowerCase();
      streamFiltrado = streamFiltrado.filter(h ->
          (h.getTitulo() != null && h.getTitulo().toLowerCase().contains(kw)) ||
              (h.getDescripcion() != null && h.getDescripcion().toLowerCase().contains(kw))
      );
    }

    // Filtro: Categoría
    if (categoria != null && !categoria.isBlank()) {
      // Usamos contains o equalsIgnoreCase según prefieras.
      // equalsIgnoreCase es mejor para selectores exactos.
      streamFiltrado = streamFiltrado.filter(h ->
          h.getCategoria() != null && h.getCategoria().equalsIgnoreCase(categoria)
      );
    }

    // Filtro: Fecha Inicio
    if (fechaInicio != null) {
      streamFiltrado = streamFiltrado.filter(h -> {
        LocalDateTime fechaHecho = h.getFechaAcontecimiento();
        return fechaHecho != null && !fechaHecho.toLocalDate().isBefore(fechaInicio);
      });
    }

    // Filtro: Fecha Fin
    if (fechaFin != null) {
      streamFiltrado = streamFiltrado.filter(h -> {
        LocalDateTime fechaHecho = h.getFechaAcontecimiento();
        return fechaHecho != null && !fechaHecho.toLocalDate().isAfter(fechaFin);
      });
    }

    // 6. Paginación Manual sobre la lista filtrada
    List<Hecho> hechosFinales = streamFiltrado.toList();

    int totalElements = hechosFinales.size();
    int totalPages = (int) Math.ceil((double) totalElements / limit);
    int fromIndex = Math.min(page * limit, totalElements);
    int toIndex = Math.min(fromIndex + limit, totalElements);

    // Cortamos la sublista para la página actual
    List<HechoDTOOutput> content = HechoMapper.hechoToDTO(hechosFinales.subList(fromIndex, toIndex));

    // 7. Retornar DTO
    return new PaginacionResponseDTO<>(
        content,
        page,
        limit,
        totalElements,
        totalPages
    );
  }

  @Transactional
  @Override
  public void refrescarColecciones() throws IOException, InterruptedException {
    //TRAER LAS COLECCIONES
    List<Coleccion> colecciones = coleccionRepository.findAll();

    // Cache para NO volver a llamar a la misma fuente varias veces
    //MAPEA por idFuente y List<Hechos> de esa fuente
    Map<Long, List<Hecho>> cacheFuentes = new HashMap<>();

    // Hechos ya existentes (para evitar duplicados)
    Map<String, Hecho> hechosPorTitulo = hechoRepository.findAll()
        .stream()
        .collect(Collectors.toMap(Hecho::getTitulo, h -> h, (a,b)->a));

    for (Coleccion coleccion : colecciones) {
      logger.info("=== Refrescando colección: {} (ID: {}) ===",
              coleccion.getTitulo(), coleccion.getId());

      List<Hecho> hechosDeColeccion = new ArrayList<>();

      for (FuenteAlt fuente : coleccion.getFuentes()) {
        logger.info("Procesando fuente: (Tipo: {})",
                fuente.getTipo());
        List<Hecho> hechosFuente;

        //Si ya llamamos a esta fuente, uso el cache
        if (cacheFuentes.containsKey(fuente.getId())) {
          hechosFuente = cacheFuentes.get(fuente.getId());
          logger.info("Usando caché para fuente {}", fuente.getId());
        } else {
          // Primera vez? llamo al servicio correspondiente
          hechosFuente = switch (fuente.getTipo()) {
            case ESTATICA -> estaticaService.obtenerHechos(fuente);
            case PROXY -> proxyService.obtenerHechos(fuente);
            case DINAMICA -> dinamicaService.obtenerHechos(fuente);
            default -> throw new IllegalArgumentException("Tipo de fuente desconocido");
          };
          logger.info("Traídos {} hechos de fuente {}",
                  hechosFuente.size(), fuente.getId());

          // Cacheo los hechos traidos por la fuente
          cacheFuentes.put(fuente.getId(), hechosFuente);
          fuente.setCantHechos(hechosFuente.size());
          fuenteRepository.save(fuente);
        }

        // Asigno origen de fuente
        for (Hecho hecho : hechosFuente) {
          hecho.getFuenteDeOrigen().add(fuente);
        }
        hechosDeColeccion.addAll(hechosFuente);
      }
      logger.info("Total hechos antes de filtrar: {}", hechosDeColeccion.size());
      // Filtrar: conservar solo los hechos nuevos por título

      List<Hecho> hechosNuevos = new ArrayList<>();
      List<Hecho> hechosExistentes = new ArrayList<>();

      for (Hecho hecho : hechosDeColeccion) {
        if (hechosPorTitulo.containsKey(hecho.getTitulo())) {
          // Hecho ya existe, usar la instancia de la BD
          hechosExistentes.add(hechosPorTitulo.get(hecho.getTitulo()));
        } else {
          // Hecho nuevo
          hechosNuevos.add(hecho);
        }
      }
      logger.info("Hechos existentes a asociar: {}", hechosExistentes.size());


      logger.info("Hechos nuevos (no duplicados por título): {}", hechosNuevos.size());
      // Normalización
      List<Hecho> hechosNormalizados = new ArrayList<>();
      for (Hecho hecho : hechosNuevos) {
        if (!normalizadorService.estaDuplicado(hecho.getTitulo())) {
          hechosNormalizados.add(hecho);
          hechosPorTitulo.put(hecho.getTitulo(), hecho);
        }else {
          logger.warn("Hecho duplicado detectado por normalizador: {}", hecho.getTitulo());
        }
      }

      logger.info("Hechos normalizados a persistir: {}", hechosNormalizados.size());


      // Persistir hechos nuevos
      hechoRepository.saveAll(hechosNormalizados);

      // Asociar nuevos hechos a la colección
      Set<Hecho> hechosActuales = new HashSet<>(coleccion.getHechos());
      hechosActuales.addAll(hechosNormalizados);  // Agregar nuevos
      hechosActuales.addAll(hechosExistentes);     // Agregar existentes

      coleccion.setHechos(new ArrayList<>(hechosActuales));
      coleccionRepository.save(coleccion);

      logger.info("Total hechos en colección después del refresco: {}", coleccion.getHechos().size());
      logger.info("=== Finalizado refresco de colección {} ===\n", coleccion.getId());
    }
  }

  //------------------------------------
  //UPDATE ALGORITMO
  //------------------------------------
  @Override
  public void modificarAlgoritmoConsenso(Long idColeccion, TiposAlgoritmos algoritmoConsenso) {
    Coleccion coleccion = coleccionRepository.findById(idColeccion)
        .orElseThrow(() -> new RuntimeException("Coleccion no encontrada con id: " + idColeccion));
    coleccion.setAlgoritmoConsenso(algoritmoConsenso);
    coleccionRepository.save(coleccion);
  }

  //------------------------------------
  //UPDATE FUENTES
  //------------------------------------
  @Override
  public void agregarFuenteAColeccion(Long idColeccion, FuenteAlt fuente) {
    Coleccion coleccion = coleccionRepository.findById(idColeccion)
        .orElseThrow(() -> new RuntimeException("Coleccion no encontrada con id: " + idColeccion));

    // VALIDACIÓN: no agregar dos veces la misma fuente
    if (coleccion.getFuentes().contains(fuente)) {
      throw new RuntimeException("La colección ya contiene la fuente con id: " + fuente.getId());
    }

    // 1) Agregar fuente
    coleccion.agregarFuente(fuente);

    // 2) Traer hechos SOLO de esta fuente (corrección: recibe lista)
    List<Hecho> hechosNuevos = hechoRepository
        .findDistinctByFuenteDeOrigen_IdIn(List.of(fuente.getId()));

    // 3) Filtrar por criterios de la colección
    List<Criterio> criterios = coleccion.getCriterios();

    List<Hecho> hechosFiltrados = hechosNuevos.stream()
        .filter(h -> criterios.stream().allMatch(c -> c.cumpleCriterio(h)))
        .toList();

    // 4) Agregar hechos evitando duplicados
    Set<Hecho> hechosActuales = new HashSet<>(coleccion.getHechos());
    hechosActuales.addAll(hechosFiltrados);

    // 5) Guardar
    coleccion.setHechos(new ArrayList<>(hechosActuales));
    coleccionRepository.save(coleccion);
  }

  @Override
  public void quitarFuenteDeColeccion(Long idColeccion, FuenteAlt fuente) {
    Coleccion coleccion = coleccionRepository.findById(idColeccion)
        .orElseThrow(() -> new RuntimeException("Coleccion no encontrada con id: " + idColeccion));

    // VALIDACIÓN: no quitar una fuente que no está en la colección
    if (!coleccion.getFuentes().contains(fuente)) {
      throw new RuntimeException("La colección no contiene la fuente con id: " + fuente.getId());
    }

    // 1) Quitar fuente
    coleccion.eliminarFuente(fuente);

    // 2) Fuentes restantes
    List<FuenteAlt> fuentesRestantes = coleccion.getFuentes();

    // 3) Hechos actuales
    List<Hecho> hechosActuales = new ArrayList<>(coleccion.getHechos());

    // 4) Identificar hechos a eliminar
    List<Hecho> hechosAEliminar = hechosActuales.stream()
        // hechos que venían de la fuente eliminada
        .filter(h -> h.getFuenteDeOrigen().contains(fuente))
        // y que NO están asociados a otras fuentes de la colección
        .filter(h -> h.getFuenteDeOrigen().stream()
            .noneMatch(fuentesRestantes::contains))
        .toList();

    // 5) Removerlos
    hechosActuales.removeAll(hechosAEliminar);

    // 6) Guardar
    coleccion.setHechos(hechosActuales);
    coleccionRepository.save(coleccion);
  }

  public void borrarHecho(Long idColeccion, Hecho hecho) {
    //coleccionRepository.delete(idColeccion, hecho);
  }
}

