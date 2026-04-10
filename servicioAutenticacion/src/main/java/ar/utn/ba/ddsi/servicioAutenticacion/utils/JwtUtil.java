package ar.utn.ba.ddsi.servicioAutenticacion.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;

import java.security.Key;
import java.util.Date;

/*
Verifica la firma del JWT
Comprueba que no esté expirado
Retorna el username si el token es válido
Si falla, lanza una excepción y se devuelve 401 Unauthorized.
 */

public class JwtUtil {
  @Getter
  private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  private static final long ACCESS_TOKEN_VALIDITY = 15 * 60 * 1000; // 15 min
  private static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 días

  public static String generarAccessToken(Long id, String username, String rol) {
    return Jwts.builder()
        .setSubject(username)
        .claim("id", id)
        .claim("username", username)
        .claim("rol", rol)
        .setIssuer("gestion-usuarios-server")
        .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
        .signWith(key)
        .compact();
  }

  public static String generarRefreshToken(Long id, String username, String rol) {
    return Jwts.builder()
        .setSubject(username)
        .claim("id", id)
        .claim("username", username)
        .claim("rol", rol)
        .claim("type", "refresh")
        .setIssuer("gestion-usuarios-server")
        .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
        .signWith(key)
        .compact();
  }

  public static Claims validarToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}