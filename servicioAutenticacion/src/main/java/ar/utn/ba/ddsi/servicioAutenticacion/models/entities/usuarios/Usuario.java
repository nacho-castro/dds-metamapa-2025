package ar.utn.ba.ddsi.servicioAutenticacion.models.entities.usuarios;

import ar.utn.ba.ddsi.servicioAutenticacion.models.entities.usuarios.password.Password;
import ar.utn.ba.ddsi.servicioAutenticacion.models.entities.usuarios.rol.TipoRoles;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="usuario")
public class Usuario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "nombre", nullable = false)
  private String nombre;

  @OneToOne(cascade = CascadeType.ALL, optional = false)
  @JoinColumn(name = "password_id", referencedColumnName = "id", nullable = false, unique = true)
  @JsonIgnore
  private Password password;

  @Column(name = "edad", nullable = false)
  private Integer edad;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_roles")
  private TipoRoles rol;


  public Boolean estaRegistrado() {
    return this.nombre != null && this.password != null;
  }

  public Boolean esAdministrador() {
    return this.rol.equals(TipoRoles.ADMINISTRADOR);
  }

  public void updateUsuario(Usuario nuevo) {
    Optional.ofNullable(nuevo.getEmail()).ifPresent(this::setEmail);
    Optional.ofNullable(nuevo.getNombre()).ifPresent(this::setNombre);
    Optional.ofNullable(nuevo.getEdad()).ifPresent(this::setEdad);
    Optional.ofNullable(nuevo.getRol()).ifPresent(this::setRol);
  }
}
