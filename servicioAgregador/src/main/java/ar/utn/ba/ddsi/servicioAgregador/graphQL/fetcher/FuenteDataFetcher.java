package ar.utn.ba.ddsi.servicioAgregador.graphQL.fetcher;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.FuenteAlt;
import ar.utn.ba.ddsi.servicioAgregador.models.repositories.IFuenteRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FuenteDataFetcher implements DataFetcher<FuenteAlt> {

  @Autowired
  private IFuenteRepository fuenteRepository;

  @Override
  public FuenteAlt get(DataFetchingEnvironment dataFetchingEnvironment) {
    Long id = dataFetchingEnvironment.getArgument("id");
    return fuenteRepository.findById(id).orElse(null);
  }
}
