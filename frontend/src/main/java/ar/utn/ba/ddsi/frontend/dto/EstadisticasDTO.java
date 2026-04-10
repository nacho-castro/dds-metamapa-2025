package ar.utn.ba.ddsi.frontend.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasDTO {
  private Long id;
  private String tipo;
  private String clave;
  private Long valor;
  private LocalDateTime fechaCalculo;
  private Long coleccionId;
}
