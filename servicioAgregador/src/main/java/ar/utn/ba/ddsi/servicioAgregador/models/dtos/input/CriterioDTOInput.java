package ar.utn.ba.ddsi.servicioAgregador.models.dtos.input;

import lombok.Data;

@Data
public class CriterioDTOInput {
  private String tipo;  // "FECHA" | "CATEGORIA" | "TITULO"
  private String valor1; // puede ser categoria, titulo, minFecha
  private String valor2; // opcional: maxFecha
}