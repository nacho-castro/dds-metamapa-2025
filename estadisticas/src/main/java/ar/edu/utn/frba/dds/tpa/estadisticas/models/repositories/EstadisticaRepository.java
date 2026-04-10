package ar.edu.utn.frba.dds.tpa.estadisticas.models.repositories;

import ar.edu.utn.frba.dds.tpa.estadisticas.models.entities.Estadistica;
import ar.edu.utn.frba.dds.tpa.estadisticas.models.entities.TipoEstadistica;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadisticaRepository extends JpaRepository<Estadistica, Long> {
  //TRAE TODAS HISTORICAS
  List<Estadistica> findByTipo(TipoEstadistica tipo);

  //TRAER ULTIMAS DIARIAS
  @Query("""
  SELECT e
  FROM Estadistica e
  WHERE e.tipo = :tipo
    AND FUNCTION('DATE', e.fechaCalculo) = (
      SELECT MAX(FUNCTION('DATE', e2.fechaCalculo))
      FROM Estadistica e2
      WHERE e2.tipo = :tipo
    )""")
  List<Estadistica> findUltimasPorTipo(@Param("tipo") TipoEstadistica tipo);

}
