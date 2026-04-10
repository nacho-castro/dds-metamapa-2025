package ar.edu.utn.frba.dds.tpa.estadisticas.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "estadisticas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Estadistica {

  @Id
  @GeneratedValue
  private Long id;

  @Enumerated(EnumType.STRING)
  private TipoEstadistica tipo;

  private Long coleccionId;   // nullable
  private String categoria;   // nullable

  private String clave;       // ej: provincia / hora / categoria
  private Long valor;         // cantidad

  private LocalDateTime fechaCalculo;
}
