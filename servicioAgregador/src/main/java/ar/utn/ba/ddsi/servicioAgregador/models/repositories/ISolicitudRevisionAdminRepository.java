package ar.utn.ba.ddsi.servicioAgregador.models.repositories;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.Estado;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.SolicitudEliminacion;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.SolicitudRevisionAdmin;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISolicitudRevisionAdminRepository extends JpaRepository<SolicitudRevisionAdmin, Long> {
}
