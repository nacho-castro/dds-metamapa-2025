package ar.utn.ba.ddsi.servicioAutenticacion.filters;

import ar.utn.ba.ddsi.servicioAutenticacion.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/*
Verificar tokens JWT y autenticar al usuario automáticamente.

Cada vez llega una petición HTTP, busca el header HTTP "Authorization".
Verifica que exista y comience con "Bearer " (el formato estándar de JWT).

Extrae el token y llama a JwtUtil para validarlo
 */

public class JwtAuthenticationFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

    String header = request.getHeader("Authorization");

    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7);
      try {
        Claims claims = JwtUtil.validarToken(token);

        Long id = claims.get("id", Long.class);
        String username = claims.get("username", String.class);
        String rol = claims.get("rol", String.class);

        // Convertimos el rol a formato Spring Security
        SimpleGrantedAuthority authority =
            new SimpleGrantedAuthority("ROLE_" + rol);

        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singletonList(authority)
            );

        // Guardamos usuario + rol
        SecurityContextHolder.getContext().setAuthentication(auth);

      } catch (Exception e) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
        return;
      }
    } else {
      System.out.println("No hay token de autorización");
    }

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();

    // No aplicar el filtro JWT a los endpoints públicos de autenticación
    return path.equals("/api/auth") || path.equals("/api/auth/refresh");
  }
}
