package ar.utn.ba.ddsi.servicioAgregador.models.repositories;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IHechoRepository extends JpaRepository<Hecho,Long> {
  Optional<Hecho> findByTituloAndDescripcion(String titulo, String descripcion);

  List<Hecho> findDistinctByFuenteDeOrigen_IdIn(List<Long> fuenteIds); //TRAER HECHOS DE LAS FUENTES SIN REPETIRLOS

  int countByFuenteDeOrigen_Id(Long idFuente);

  @Query("""
        SELECT h FROM Hecho h
        WHERE (:titulo IS NULL OR LOWER(h.titulo) LIKE LOWER(CONCAT('%', :titulo, '%')))
          AND (:descripcion IS NULL OR LOWER(h.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%')))
          AND (:fechaDesde IS NULL OR h.fechaAcontecimiento >= :fechaDesde)
          AND (:fechaHasta IS NULL OR h.fechaAcontecimiento <= :fechaHasta)
    """)
  Page<Hecho> buscarConFiltros(
      @Param("titulo") String titulo,
      @Param("descripcion") String descripcion,
      @Param("fechaDesde") LocalDateTime fechaDesde,
      @Param("fechaHasta") LocalDateTime fechaHasta,
      Pageable pageable
  );
}
