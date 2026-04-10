package ar.utn.ba.ddsi.servicioAgregador.services;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.input.HechoDinamicoDTOInput;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.FuenteAlt;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;

import java.util.List;

public interface IFuenteDinamicaService {
  public List<Hecho> obtenerHechos(FuenteAlt fuente);
  public Hecho subirHecho(FuenteAlt fuente, HechoDinamicoDTOInput hecho, String token);
  public Hecho editarHecho(FuenteAlt fuente, Long id, HechoDinamicoDTOInput hechoNuevo, String token);
}
//LA FUENTE DINAMICA HACE CRUD DE HECHOS