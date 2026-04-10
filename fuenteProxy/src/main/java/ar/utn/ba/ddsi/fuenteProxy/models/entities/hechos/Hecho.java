package ar.utn.ba.ddsi.fuenteProxy.models.entities.hechos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Hecho {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private Lugar lugarAcontecimiento;
  private LocalDateTime fechaAcontecimiento;
  private LocalDateTime fechaCarga;

  private Boolean activo;
  private List<Etiqueta> etiquetas;

  public void updateHecho(Hecho hechoNuevo) {
    this.titulo = hechoNuevo.getTitulo();
    this.descripcion = hechoNuevo.getDescripcion();
    this.categoria = hechoNuevo.getCategoria();
    this.lugarAcontecimiento = hechoNuevo.getLugarAcontecimiento();
    this.fechaAcontecimiento = hechoNuevo.getFechaAcontecimiento();
  }

  public void desactivarHecho() {
    this.activo = false;
  }
}

