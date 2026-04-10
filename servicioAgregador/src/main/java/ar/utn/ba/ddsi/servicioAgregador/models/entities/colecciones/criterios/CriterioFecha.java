package ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.criterios;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter @Getter
@Entity
@DiscriminatorValue("FECHA")
public class CriterioFecha extends Criterio {

  private LocalDate minFecha;
  private LocalDate maxFecha;

  public CriterioFecha(LocalDate minFecha, LocalDate maxFecha) {
    this.minFecha = minFecha;
    this.maxFecha = maxFecha;
  }

  @Override
  public boolean cumpleCriterio(Hecho hecho) {
    LocalDate fechaHecho = hecho.getFechaAcontecimiento().toLocalDate();
    return !fechaHecho.isBefore(minFecha) && !fechaHecho.isAfter(maxFecha);
  }
}
