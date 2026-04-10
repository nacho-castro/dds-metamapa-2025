package ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.criterios;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@DiscriminatorValue("CATEGORIA")
@Getter @Setter
public class CriterioCategoria extends Criterio {

  @Column(name = "descripcion_categoria")
  private String categoria;

  public CriterioCategoria(String categoria) {
    this.categoria = categoria;
  }

  @Override
  public boolean cumpleCriterio(Hecho hecho) {
    return hecho.getCategoria() != null && hecho.getCategoria().equalsIgnoreCase(categoria);
  }
}
