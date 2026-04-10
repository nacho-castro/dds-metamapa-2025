package ar.utn.ba.ddsi.servicioAutenticacion.models.entities.usuarios.password;

import ar.utn.ba.ddsi.servicioAutenticacion.models.entities.usuarios.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "passwords" ,
        uniqueConstraints = @UniqueConstraint(columnNames = "usuario_id"))
public class Password {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "hash", nullable = false)
  private String hash;

  @OneToOne(mappedBy = "password")
  private Usuario usuario;

  @Column(name = "last_changed_at", nullable = false)
  private LocalDateTime lastChangedAt;

  @PrePersist
  public void onCreate() {
    if (lastChangedAt == null) {
      lastChangedAt = LocalDateTime.now();
    }
  }

  @PreUpdate
  public void onUpdate() {
    lastChangedAt = LocalDateTime.now();
  }

  public void setNewHash(String encodedHash) {
    this.hash = encodedHash;
  }
}
