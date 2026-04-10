package ar.utn.ba.ddsi.fuenteEstatica.models.entities.hechos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
  private List<Etiqueta> etiquetas = new ArrayList<>();

  public Hecho(String titulo, String descripcion, String categoria, Lugar lugar, LocalDateTime fecha) {
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.categoria = categoria;
    this.lugarAcontecimiento = lugar;
    this.fechaAcontecimiento = fecha;
    this.fechaCarga = LocalDateTime.now();
  }

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
