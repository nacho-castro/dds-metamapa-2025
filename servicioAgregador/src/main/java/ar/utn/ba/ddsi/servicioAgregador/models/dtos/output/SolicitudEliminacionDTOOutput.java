package ar.utn.ba.ddsi.servicioAgregador.models.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SolicitudEliminacionDTOOutput {
    private Long id;
    private Long idHecho; // ID del hecho que se solicita eliminar
    private String motivoBorrado;
    private String estado;
    private LocalDateTime fecha;
}
