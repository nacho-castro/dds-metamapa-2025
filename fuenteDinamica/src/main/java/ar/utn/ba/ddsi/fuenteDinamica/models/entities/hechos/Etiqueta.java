package ar.utn.ba.ddsi.fuenteDinamica.models.entities.hechos;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Etiqueta {
  //DATA CLASS
  private String nombre;
}