package ar.utn.ba.ddsi.fuenteProxy.models.dtos.input.external.api;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponseDTO {
  private List<ApiHechoDTO> data;
}
