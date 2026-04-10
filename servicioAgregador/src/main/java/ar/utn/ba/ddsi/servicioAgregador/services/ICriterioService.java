package ar.utn.ba.ddsi.servicioAgregador.services;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.CriterioDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.criterios.Criterio;

public interface ICriterioService {
  Criterio crearCriterio(CriterioDTOInput dto);
  Criterio actualizarCriterio(Long id, CriterioDTOInput dto);
  void eliminarCriterio(Long id);
  Criterio obtener(Long id);
  java.util.List<Criterio> listar();
}
