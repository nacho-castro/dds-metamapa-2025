package ar.utn.ba.ddsi.servicioAgregador.controller;

import graphql.GraphQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/fuentesQL")
public class FuenteControllerQL {

  @Autowired
  private GraphQL graphQL;

  @PostMapping
  public ResponseEntity<Object> fuentesQL(@RequestBody String query) {
    return ResponseEntity.ok(graphQL.execute(query));
  }
}
