package ar.utn.ba.ddsi.frontend.providers;

import ar.utn.ba.ddsi.frontend.dto.input.AuthResponseDTO;
import ar.utn.ba.ddsi.frontend.dto.input.RolesPermisosDTO;
import ar.utn.ba.ddsi.frontend.dto.output.LoginRequest;
import ar.utn.ba.ddsi.frontend.services.AuthApiService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

/*
Solicita los Tokens al servicio externo y se encarga de guardarlos
AccessToken + RefreshToken
Roles + Permisos
*/

@Component
public class CustomAuthProvider implements AuthenticationProvider {
  private static final Logger log = LoggerFactory.getLogger(CustomAuthProvider.class);
  private final AuthApiService externalAuthService;

  public CustomAuthProvider(AuthApiService externalAuthService) {
    this.externalAuthService = externalAuthService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String password = authentication.getCredentials().toString();

    try {
      // Llamada a servicio externo para obtener tokens
      LoginRequest loginRequest = new LoginRequest(username, password);
      AuthResponseDTO authResponse = externalAuthService.login(loginRequest);

      if (authResponse == null) {
        throw new BadCredentialsException("Usuario o contraseña inválidos");
      }

      log.info("Usuario logeado! Configurando variables de sesión");
      ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
      HttpServletRequest request = attributes.getRequest();

      //Guardar tokens en sesion
      request.getSession().setAttribute("accessToken", authResponse.getAccessToken());
      request.getSession().setAttribute("refreshToken", authResponse.getRefreshToken());
      request.getSession().setAttribute("username", username);

      log.info("Buscando roles y permisos del usuario");
      RolesPermisosDTO rolesPermisos = externalAuthService.getRolesPermisos(authResponse.getAccessToken());

      log.info("Cargando roles y permisos del usuario en sesión");
      request.getSession().setAttribute("rol", rolesPermisos.getRol());
      request.getSession().setAttribute("permisos", rolesPermisos.getPermisos());

      List<GrantedAuthority> authorities = new ArrayList<>();
      if (rolesPermisos.getPermisos() != null) {
        rolesPermisos.getPermisos().forEach(permiso -> {
          authorities.add(new SimpleGrantedAuthority(permiso.name()));
        });
      } else {
        log.warn("Los permisos del rol {} son null", rolesPermisos.getRol());
      }
      authorities.add(new SimpleGrantedAuthority("ROLE_" + rolesPermisos.getRol().name()));

      return new UsernamePasswordAuthenticationToken(username, password, authorities);

    } catch (RuntimeException e) {
      throw new BadCredentialsException("Error en el sistema de autenticación: " + e.getMessage());
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
