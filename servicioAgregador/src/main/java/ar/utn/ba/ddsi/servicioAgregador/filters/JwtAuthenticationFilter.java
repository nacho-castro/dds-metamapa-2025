package ar.utn.ba.ddsi.servicioAgregador.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
Proteger rutas securizadas.
- Cada vez llega una petición HTTP, busca el header HTTP "Authorization".
- Llama al AuthManager para validar contra el AuthService externo

OncePerRequestFilter: hook nativo de Spring para decidir
en qué requests el filtro no se aplica.
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

    // No filtrar...
    if (method.equalsIgnoreCase("GET")) return true;
    if (path.equals("/api/colecciones/refrescar")) return true;

    return false;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token faltante o mal formado");
      return;
    }

    String token = authHeader.substring(7);

    try {
      boolean valido = authManager.validarToken(token);
      if (!valido) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
        return;
      }
    } catch (Exception e) {
      e.printStackTrace();
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error en validación de token");
      return;
    }

    filterChain.doFilter(request, response);
  }
}

