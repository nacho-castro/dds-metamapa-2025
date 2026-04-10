package ar.edu.utn.frba.dds.tpa.estadisticas.models.dtos;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
  private List<T> content;
  private int page;
  private int limit;
  private long totalElements;
  private int totalPages;
  private boolean hasNext;
  private boolean hasPrevious;

  public List<T> getContent() {
    return content;
  }

  public void setContent(List<T> content) {
    this.content = content;
  }
}

