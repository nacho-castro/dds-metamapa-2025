package ar.utn.ba.ddsi.servicioAgregador.services.impl;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.FuenteDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.FuenteDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.FuenteAlt;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.FuenteMapper;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.TipoFuente;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.servicioAgregador.models.repositories.IFuenteRepository;
import ar.utn.ba.ddsi.servicioAgregador.models.repositories.IHechoRepository;
import ar.utn.ba.ddsi.servicioAgregador.services.IFuenteDinamicaService;
import ar.utn.ba.ddsi.servicioAgregador.services.IFuenteEstaticaService;
import ar.utn.ba.ddsi.servicioAgregador.services.IFuenteProxyService;
import ar.utn.ba.ddsi.servicioAgregador.services.IFuenteService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FuenteService implements IFuenteService {

  private IFuenteRepository fuenteRepository;
  private IFuenteDinamicaService dinamicaService;
  private IFuenteEstaticaService estaticaService;
  private IFuenteProxyService proxyService;
  private IHechoRepository hechoRepository;

  public FuenteService(IFuenteRepository fuenteRepository, IFuenteDinamicaService dinamicaService,
                       IFuenteEstaticaService estaticaService, IFuenteProxyService proxyService, IHechoRepository hechoRepository) {
    this.fuenteRepository = fuenteRepository;
    this.dinamicaService = dinamicaService;
    this.estaticaService = estaticaService;
    this.proxyService = proxyService;
    this.hechoRepository = hechoRepository;
  }

  @Override
  public FuenteDTOOutput guardarFuente(FuenteDTOInput fuente) {
    //1. PERSISTIR FUENTE CREADA
    FuenteAlt fuenteNueva = FuenteMapper.dtoToFuente(fuente);
    fuenteRepository.save(fuenteNueva);

    //2. OBTENER HECHOS DE ESA FUENTE
    List<Hecho> hechosObtenidos = obtenerHechosPorTipo(fuenteNueva);

    for (Hecho h : hechosObtenidos) {
      Hecho existente = hechoRepository
          .findByTituloAndDescripcion(h.getTitulo(), h.getDescripcion())
          .orElse(null);

      if (existente != null) {
        // Si ya existe, solo agregá la fuente
        if (!existente.getFuenteDeOrigen().contains(fuenteNueva)) {
          existente.getFuenteDeOrigen().add(fuenteNueva);
        }
        hechoRepository.save(existente);
      } else {
        // Nuevo: asignar fuente y guardar
        h.setFuenteDeOrigen(new ArrayList<>(List.of(fuenteNueva)));
        hechoRepository.save(h);
      }
    }

    // 3. Calcular cuántos hechos tiene asociados esta fuente
    int total = hechoRepository.countByFuenteDeOrigen_Id(fuenteNueva.getId());
    fuenteNueva.setCantHechos(total);
    fuenteRepository.save(fuenteNueva);

    return FuenteMapper.fuenteToDTO(fuenteNueva);
  }

  @Override
  public List<FuenteDTOOutput> obtenerFuentes() {
    List<FuenteAlt> fuentes = fuenteRepository.findAll();
    return FuenteMapper.fuenteToDTO(fuentes);
  }

  @Override
  public FuenteDTOOutput buscarFuente(Long id) {
    FuenteAlt fuente = fuenteRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Fuente no encontrada con id: " + id));
    return FuenteMapper.fuenteToDTO(fuente);
  }

  @Override
  public FuenteAlt encontrarFuente(Long id) {
    return fuenteRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Fuente no encontrada con id: " + id));
  }

  @Override
  public FuenteDTOOutput actualizarFuente(Long id, FuenteDTOInput nueva) {
    FuenteAlt fuente = fuenteRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Fuente no encontrada con id: " + id));

    // actualizar campos
    fuente.update(FuenteMapper.dtoToFuente(nueva));

    fuenteRepository.save(fuente);
    return FuenteMapper.fuenteToDTO(fuente);
  }

  @Override
  public FuenteDTOOutput borrarFuente(Long id) {
    FuenteAlt fuente = fuenteRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Fuente no encontrada con id: " + id));

    fuenteRepository.delete(fuente);
    return FuenteMapper.fuenteToDTO(fuente);
  }

  private List<Hecho> obtenerHechosPorTipo(FuenteAlt fuente) {
    switch (fuente.getTipo()) {
      case DINAMICA:
        return dinamicaService.obtenerHechos(fuente);
      case ESTATICA:
        return estaticaService.obtenerHechos(fuente);
      case PROXY:
        return proxyService.obtenerHechos(fuente);
      default:
        throw new IllegalArgumentException("Tipo de fuente desconocido: " + fuente.getTipo());
    }
  }

}
