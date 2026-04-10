package ar.utn.ba.ddsi.fuenteDinamica.services.impl;

import ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos.Hecho;
import ar.utn.ba.ddsi.fuenteDinamica.models.repositories.IHechoRepository;
import ar.utn.ba.ddsi.fuenteDinamica.services.IHechoService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class HechoService implements IHechoService {
  private IHechoRepository hechoRepository;

  public HechoService(IHechoRepository repository) {
    this.hechoRepository = repository;
  }

  @Override
  public Hecho crearHecho(Hecho hecho) {
    hechoRepository.save(hecho);
    return hecho;
  }

  @Override
  public List<Hecho> obtenerHechos() {
    return hechoRepository.findAll();
  }

  @Override
  public Hecho editarHecho(Long id, Hecho hechoNuevo) { //titulo y descripcion lo podemos usar parabuscar hechos
    Hecho hecho = hechoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Hecho no encontrado con id: " + id));
    hecho.updateHecho(hechoNuevo);
    hechoRepository.save(hecho);
    return hecho;
  }

  @Override
  public Hecho obtenerHechoPorId(Long id){
    return hechoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Hecho no encontrado con id: " + id));
  }

  @Override
  public Hecho borrarHecho(Long id) {
    Hecho hecho = hechoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Hecho no encontrado con id: " + id));
    hechoRepository.delete(hecho);
    return hecho;
  }
}

