package ar.utn.ba.ddsi.fuenteProxy.models.entities.hechos.contenido;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "multimedia")
@Getter
@Setter
public class Multimedia {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_multimedia")
  private TipoMultimedia tipoMultimedia;

  @Column(name = "url", nullable=true)
  private String urlMultimedia;
  @Column(name = "fecha_creacion", nullable=false)
  private LocalDateTime fechaCreacion;
  @Column(name = "fecha_modificacion", nullable=false)
  private LocalDateTime fechaModificacion;
}
