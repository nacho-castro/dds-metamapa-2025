package ar.utn.ba.ddsi.servicioAgregador.models.repositories;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.Estado;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.SolicitudEdicion;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.SolicitudEliminacion;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ISolicitudEdicionRepository extends JpaRepository<SolicitudEdicion, Long> {
  @Query("SELECT s FROM SolicitudEdicion s ORDER BY " +
      "CASE WHEN s.estadoActual = 'PENDIENTE' THEN 0 ELSE 1 END ASC, " +
      "s.fecha DESC")
  Page<SolicitudEdicion> findAllOrdenadoPorPrioridad(Pageable pageable);
}
