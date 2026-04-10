package ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.criterios;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
@Entity
@DiscriminatorValue("TITULO")
public class CriterioTitulo extends Criterio {

  @Column(name = "titulo")
  private String titulo;

  public CriterioTitulo(String titulo) {
    this.titulo = titulo;
  }

  @Override
  public boolean cumpleCriterio(Hecho hecho) {
    return hecho.getTitulo() != null && hecho.getTitulo().contains(titulo);
  }
}
