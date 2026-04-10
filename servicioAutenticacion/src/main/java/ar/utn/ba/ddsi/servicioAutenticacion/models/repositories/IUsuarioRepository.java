package ar.utn.ba.ddsi.servicioAutenticacion.models.repositories;

import ar.utn.ba.ddsi.servicioAutenticacion.models.entities.usuarios.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
  public Optional<Usuario> findByEmail(String email);

  @Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.password = :password")
  Optional<Usuario> validarLogin(@Param("email") String email, @Param("password") String password);

/* chad
 public void save(Usuario usuario);
  public void saveAll(List<Usuario> hechos);
  public List<Usuario> findAll();
  public Usuario findById(Long id);
  public void delete(Usuario usuario);
  */
}