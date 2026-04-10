package ar.utn.ba.ddsi.servicioAgregador.graphQL.fetcher;

import ar.utn.ba.ddsi.servicioAgregador.models.entities.fuentes.FuenteAlt;
import ar.utn.ba.ddsi.servicioAgregador.models.repositories.IFuenteRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AllFuentesDataFetcher implements DataFetcher<List<FuenteAlt>> {

  @Autowired
  private IFuenteRepository fuenteRepository;

  @Override
  public List<FuenteAlt> get(DataFetchingEnvironment dataFetchingEnvironment) {
    Long id = dataFetchingEnvironment.getArgument("id");
    return Collections.singletonList(fuenteRepository.findById(id).orElse(null));
  }
}
