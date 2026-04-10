package ar.utn.ba.ddsi.frontend.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CriterioDTOOutput {
    private Long id;
    private String categoria;
    private String titulo;
}
