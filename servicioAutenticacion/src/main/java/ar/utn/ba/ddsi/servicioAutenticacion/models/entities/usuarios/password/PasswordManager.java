package ar.utn.ba.ddsi.servicioAutenticacion.models.entities.usuarios.password;

import ar.utn.ba.ddsi.servicioAutenticacion.models.entities.usuarios.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordManager {
  private final PasswordEncoder encoder;

  public PasswordManager() {
    this.encoder = new BCryptPasswordEncoder();
  }

  @Transactional
  public void setPassword(Usuario usuario, String rawPassword) {
    String hashedPassword = encoder.encode(rawPassword);
    Password pwd = usuario.getPassword();
    if (pwd == null) {
      pwd = new Password();
      pwd.setUsuario(usuario);
      usuario.setPassword(pwd);
    }
    pwd.setNewHash(hashedPassword);
  }


  public boolean passwordMatches(Usuario usuario, String rawPassword) {
    return encoder.matches(rawPassword, usuario.getPassword().getHash());
  }
}
