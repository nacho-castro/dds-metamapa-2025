package ar.utn.ba.ddsi.frontend.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTOOutput<T> {
  private List<T> content;
  private int page;
  private int totalPages;
  private long totalElements;
  private boolean hasNext;
  private boolean hasPrevious;
}
