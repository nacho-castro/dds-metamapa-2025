package ar.utn.ba.ddsi.fuenteDinamica.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
Proteger rutas securizadas.
- Cada vez llega una petición HTTP, busca el header HTTP "Authorization".
- Llama al AuthManager para validar contra el AuthService externo
- Algunas rutas son públicas: permiten token nulo (pero si viene, lo procesamos)
*/

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final AuthManager authManager;

  public JwtAuthenticationFilter(AuthManager authManager) {
    this.authManager = authManager;
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    String method = request.getMethod();

    // No filtrar GETs
    if (method.equalsIgnoreCase("GET")) return true;

    // POST de creación de hechos. NO filtrar
    if (method.equalsIgnoreCase("POST") && path.matches(".*/hechos/?$")) {
      return true;
    }
    return false; //filtrar PUT, DELETE
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    // Si hay token, validar
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);
      try {
        boolean valido = authManager.validarToken(token);
        if (!valido) {
          response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
          return;
        }

        Long id = authManager.obtenerIdUsuario(token);
        request.setAttribute("id", id);

      } catch (Exception e) {
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error en validación de token");
        return;
      }
    }

    // En rutas públicas: token nulo => continuar sin problema
    filterChain.doFilter(request, response);
  }
}
