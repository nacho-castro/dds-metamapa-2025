package ar.utn.ba.ddsi.fuenteProxy.models.dtos.input.external.disilab;

import lombok.Data;
import java.util.List;

@Data
public class DisilabResponseDTO {
  private int current_page;
  private List<DisilabHechoDTO> data;
  private String first_page_url;
  private Integer from;
  private int last_page;
  private String last_page_url;
  private String next_page_url;
  private String path;
  private int per_page;
  private String prev_page_url;
  private Integer to;
  private int total;
}
