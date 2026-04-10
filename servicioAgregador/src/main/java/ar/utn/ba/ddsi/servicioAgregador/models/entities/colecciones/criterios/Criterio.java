package ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.criterios;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.function.Predicate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "criterio")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_criterio")
public abstract class Criterio {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // cada subclase define cómo filtrar hechos
  public abstract boolean cumpleCriterio(Hecho hecho);
}

//CRITERIO PADRE. EXISTEN DIFERENTES CRITERIOS HIJOS:
// EJ: CRITERIO POR FECHA 1. QUIERO LOS HECHOS OCURRIDOS EN 2025
// EJ: CRITERIO POR LUGAR 2. QUIERO LOS HECHOS OCURRIDOS EN MAR DEL PLATA
//EJ: CRITERIO POR TITULO
// EJ: CRITERIO POR CATEGORIA QUIERO COINCIDA EL STRING "INCENDIO"

