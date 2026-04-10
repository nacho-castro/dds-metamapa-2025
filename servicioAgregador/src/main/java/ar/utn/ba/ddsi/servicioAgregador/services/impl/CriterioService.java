package ar.utn.ba.ddsi.servicioAgregador.services.impl;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.CriterioDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.criterios.Criterio;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.criterios.CriterioCategoria;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.criterios.CriterioFecha;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.criterios.CriterioTitulo;
import ar.utn.ba.ddsi.servicioAgregador.models.repositories.ICriterioRepository;
import ar.utn.ba.ddsi.servicioAgregador.services.ICriterioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CriterioService implements ICriterioService {

  private final ICriterioRepository criterioRepository;

  public CriterioService(ICriterioRepository criterioRepository) {
    this.criterioRepository = criterioRepository;
  }

  @Override
  public Criterio crearCriterio(CriterioDTOInput dto) {
    return switch (dto.getTipo().toUpperCase()) {
      case "CATEGORIA" -> criterioRepository.save(
          new CriterioCategoria(dto.getValor1())
      );

      case "TITULO" -> criterioRepository.save(
          new CriterioTitulo(dto.getValor1())
      );

      case "FECHA" -> criterioRepository.save(
          new CriterioFecha(
              LocalDate.parse(dto.getValor1()),
              LocalDate.parse(dto.getValor2())
          )
      );

      default -> throw new IllegalArgumentException("Tipo de criterio inválido");
    };
  }

  @Override
  public Criterio actualizarCriterio(Long id, CriterioDTOInput dto) {
    Criterio criterio = criterioRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Criterio no encontrado"));

    if (criterio instanceof CriterioCategoria cc) {
      cc.setCategoria(dto.getValor1());
    }

    else if (criterio instanceof CriterioTitulo ct) {
      ct.setTitulo(dto.getValor1());
    }

    else if (criterio instanceof CriterioFecha cf) {
      cf.setMinFecha(LocalDate.parse(dto.getValor1()));
      cf.setMaxFecha(LocalDate.parse(dto.getValor2()));
    }

    return criterioRepository.save(criterio);
  }

  @Override
  public void eliminarCriterio(Long id) {
    criterioRepository.deleteById(id);
  }

  @Override
  public Criterio obtener(Long id) {
    return criterioRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Criterio no encontrado"));
  }

  @Override
  public java.util.List<Criterio> listar() {
    return criterioRepository.findAll();
  }
}