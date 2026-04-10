package ar.utn.ba.ddsi.servicioAgregador.config;

import ar.utn.ba.ddsi.servicioAgregador.graphQL.fetcher.AllFuentesDataFetcher;
import ar.utn.ba.ddsi.servicioAgregador.graphQL.fetcher.FuenteDataFetcher;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/*
- Crea el schema
- Conecta con FuenteDataFetcher
- Expone un bean GraphQL
- Permite que FuenteControllerQL lo consuma
*/

@Configuration
public class GraphQLProvider {

  private final AllFuentesDataFetcher allFuentesDataFetcher;
  private final FuenteDataFetcher fuenteDataFetcher;

  public GraphQLProvider(AllFuentesDataFetcher allFuentesDataFetcher,
                         FuenteDataFetcher fuenteDataFetcher) {
    this.allFuentesDataFetcher = allFuentesDataFetcher;
    this.fuenteDataFetcher = fuenteDataFetcher;
  }

  @Bean
  public GraphQL graphQL() throws IOException {

    ClassPathResource resource = new ClassPathResource("schema.graphqls");
    String sdl = new String(resource.getInputStream().readAllBytes());

    TypeDefinitionRegistry typeRegistry =
        new SchemaParser().parse(sdl);

    RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
        .type("Query", builder -> builder
            .dataFetcher("fuente", fuenteDataFetcher)
            .dataFetcher("allFuentes", allFuentesDataFetcher)
        )
        .build();

    GraphQLSchema schema =
        new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);

    return GraphQL.newGraphQL(schema).build();
  }
}