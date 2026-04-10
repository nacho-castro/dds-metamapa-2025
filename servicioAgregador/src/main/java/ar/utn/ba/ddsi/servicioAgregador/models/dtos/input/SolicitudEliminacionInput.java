package ar.utn.ba.ddsi.servicioAgregador.models.dtos.input;

import lombok.Data;

@Data
public class SolicitudEliminacionInput {
  Long idHecho;
  String motivoBorrado;
}
