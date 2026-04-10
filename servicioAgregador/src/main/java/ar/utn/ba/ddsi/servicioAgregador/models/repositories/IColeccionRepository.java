package ar.utn.ba.ddsi.servicioAgregador.models.repositories;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.colecciones.Coleccion;
import ar.utn.ba.ddsi.servicioAgregador.models.entities.hechos.Hecho;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IColeccionRepository extends JpaRepository<Coleccion, Long> {

    // Provincia con más hechos
    @Query("""
        SELECT l.provincia, COUNT(h) 
        FROM Coleccion c 
        JOIN c.hechos h 
        JOIN h.lugarAcontecimiento l 
        WHERE c.id = :idColeccion 
        GROUP BY l.provincia 
        ORDER BY COUNT(h) DESC
    """)
    List<Object[]> provinciaConMasHechos(@Param("idColeccion") Long idColeccion);

    // Categoría con más hechos
    @Query("""
        SELECT h.categoria, COUNT(h) 
        FROM Coleccion c 
        JOIN c.hechos h 
        WHERE c.id = :idColeccion 
        GROUP BY h.categoria 
        ORDER BY COUNT(h) DESC
    """)
    List<Object[]> categoriaConMasHechos(@Param("idColeccion") Long idColeccion);

    // Provincia con más hechos por categoría
    @Query("""
        SELECT h.categoria, l.provincia, COUNT(h) 
        FROM Coleccion c 
        JOIN c.hechos h 
        JOIN h.lugarAcontecimiento l 
        WHERE c.id = :idColeccion 
        GROUP BY h.categoria, l.provincia 
        ORDER BY h.categoria, COUNT(h) DESC
    """)
    List<Object[]> provinciaConMasHechosPorCategoria(@Param("idColeccion") Long idColeccion);

    // Hora del día con más hechos por categoría
    @Query("""
        SELECT h.categoria, FUNCTION('HOUR', h.fechaAcontecimiento), COUNT(h)
        FROM Coleccion c 
        JOIN c.hechos h 
        WHERE c.id = :idColeccion
        GROUP BY h.categoria, FUNCTION('HOUR', h.fechaAcontecimiento)
        ORDER BY h.categoria, COUNT(h) DESC
    """)
    List<Object[]> horaConMasHechosPorCategoria(@Param("idColeccion") Long idColeccion);

  Page<Coleccion> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);
}

