package ar.utn.ba.ddsi.frontend.dto.input;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CriterioDTOInput {
    private String tipo;
    private String valor1;
    @Nullable
    private String valor2;
}
