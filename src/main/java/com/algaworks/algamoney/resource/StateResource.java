package com.algaworks.algamoney.resource;

import com.algaworks.algamoney.repository.StateRepository;
import com.algaworks.algamoney.model.State;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/states")
public class StateResource {

  private final StateRepository repository;

  public StateResource(StateRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  @PreAuthorize("isAuthenticated()")
  public Iterable<State> listar() {
    return repository.findAll();
  }
}
