package ar.utn.ba.ddsi.servicioAgregador;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class ProdDiagnostics {

  @Bean
  public CommandLineRunner run() {
    return args -> System.out.println("Hello from Prod!");
  }
}
