package com.algaworks.algamoney.resource;

import com.algaworks.algamoney.model.City;
import com.algaworks.algamoney.repository.CityRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.algaworks.algamoney.repository.query.CityQueryExpressions.cityByStateId;

@RestController
@RequestMapping("/cities")
public class CityResource {

  private final CityRepository repository;

  public CityResource(CityRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  @PreAuthorize("isAuthenticated()")
  public List<City> search(@RequestParam Long stateId) {
    return repository.findAll(cityByStateId(stateId));
  }
}
