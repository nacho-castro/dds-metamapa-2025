package ar.utn.ba.ddsi.fuenteEstatica.models.dtos.output;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class LugarDTOOutput {
  private Double latitud;
  private Double longitud;
}
