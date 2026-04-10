package ar.utn.ba.ddsi.servicioAgregador.models.repositories;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.Estado;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.SolicitudEliminacion;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ISolicitudEliminacionRepository extends JpaRepository<SolicitudEliminacion, Long> {
  @Query("SELECT s FROM SolicitudEliminacion s ORDER BY " +
      "CASE WHEN s.estadoActual = 'PENDIENTE' THEN 0 ELSE 1 END ASC, " +
      "s.fecha DESC") // Desempate por fecha (lo más nuevo arriba)
  Page<SolicitudEliminacion> findAllOrdenadoPorPrioridad(Pageable pageable);
}
