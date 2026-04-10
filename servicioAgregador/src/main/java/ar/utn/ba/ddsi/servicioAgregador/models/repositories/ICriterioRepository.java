package ar.utn.ba.ddsi.servicioAgregador.models.repositories;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.criterios.Criterio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICriterioRepository extends JpaRepository<Criterio, Long> {
}
