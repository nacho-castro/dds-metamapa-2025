package ar.utn.ba.ddsi.frontend.config;

import ar.utn.ba.ddsi.frontend.providers.CustomAuthProvider;
import jakarta.servlet.RequestDispatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

  //componente central que autentica a los usuarios cuando se hace login.
  /*private final CustomAuthProvider customAuthProvider;

  public SecurityConfig(CustomAuthProvider customAuthProvider) {
    this.customAuthProvider = customAuthProvider;
  }*/

  @Bean
  public AuthenticationManager authManager(HttpSecurity http, CustomAuthProvider provider) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
        .authenticationProvider(provider)
        .build();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        //.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            // Rutas públicas
            .requestMatchers("/", "/acerca", "/registro").permitAll()
            .requestMatchers("/colecciones", "/colecciones/**").permitAll()
            .requestMatchers("/fuentes", "/fuentes/**").permitAll()
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/error", "/403", "/404", "/login", "/logout").permitAll()
            .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")
            .requestMatchers(HttpMethod.POST, "/hechos/**", "/hechos").permitAll()
            // Recursos estáticos (css, js, imágenes)
            .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
            // El resto requiere autenticación
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .permitAll()
        )
        .logout(logout -> logout
            .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "GET"))
            .logoutSuccessUrl("/login?logout")
            .permitAll()
        )
        .exceptionHandling(ex -> ex
            .accessDeniedHandler((request, response, accessDeniedException) -> {
              request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.UNAUTHORIZED.value());
              request.getRequestDispatcher("/error").forward(request, response);
            })
        );
    return http.build();
  }
}
