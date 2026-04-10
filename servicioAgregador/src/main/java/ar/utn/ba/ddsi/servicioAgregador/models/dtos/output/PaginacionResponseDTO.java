package ar.utn.ba.ddsi.servicioAgregador.models.dtos.output;

import java.util.List;

public class PaginacionResponseDTO<T> {
  private List<T> content;
  private int page;
  private int limit;
  private long totalElements;
  private int totalPages;
  private boolean hasNext;
  private boolean hasPrevious;

  public PaginacionResponseDTO(List<T> content, int page, int limit, long totalElements, int totalPages) {
    this.content = content;
    this.page = page;
    this.limit = limit;
    this.totalElements = totalElements;
    this.totalPages = totalPages;
    this.hasNext = page < totalPages - 1;
    this.hasPrevious = page > 0;
  }

  // Getters y setters
  public List<T> getContent() { return content; }
  public int getPage() { return page; }
  public int getLimit() { return limit; }
  public long getTotalElements() { return totalElements; }
  public int getTotalPages() { return totalPages; }
  public boolean isHasNext() { return hasNext; }
  public boolean isHasPrevious() { return hasPrevious; }
}
