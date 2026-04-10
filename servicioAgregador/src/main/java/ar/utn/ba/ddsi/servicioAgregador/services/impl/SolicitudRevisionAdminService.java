package ar.utn.ba.ddsi.servicioAgregador.services.impl;

import ar.utn.ba.ddsi.servicioAgregador.models.dtos.output.SolicitudAdminDTOOutput;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.solicitud.SolicitudRevisionAdmin;
import ar.utn.ba.ddsi.servicioAgregador.models.repositories.ISolicitudRevisionAdminRepository;
import ar.utn.ba.ddsi.servicioAgregador.services.ISolicitudRevisionAdminService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SolicitudRevisionAdminService implements ISolicitudRevisionAdminService {
  private ISolicitudRevisionAdminRepository adminRepository;

  public SolicitudRevisionAdminService(ISolicitudRevisionAdminRepository adminRepository) {
    this.adminRepository = adminRepository;
  }

  @Override
  public List<SolicitudAdminDTOOutput> buscarTodos() {
    var solicitudesAdmin = this.adminRepository.findAll();
    return solicitudesAdmin.stream().map(this::solicitudAdminToDTO).toList();
  }

  private SolicitudAdminDTOOutput solicitudAdminToDTO(SolicitudRevisionAdmin solicitud) {
    var solicitudAdminDTO = new SolicitudAdminDTOOutput();

    //Seteo atributos

    return solicitudAdminDTO;
  }

  @Override
  public void revisarSolicitud(SolicitudRevisionAdmin solicitud) {
    //TODO: habria que ver los criterios para cuando aceptar un hecho o cuando aceptar con sugerencia
  }
}
