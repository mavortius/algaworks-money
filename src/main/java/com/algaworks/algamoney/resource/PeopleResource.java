package com.algaworks.algamoney.resource;

import com.algaworks.algamoney.repository.PersonRepository;
import com.algaworks.algamoney.event.ResourceCreatedEvent;
import com.algaworks.algamoney.model.Person;
import com.algaworks.algamoney.repository.querydsl.DefaultBinder;
import com.algaworks.algamoney.service.PersonService;
import com.querydsl.core.types.Predicate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/people")
public class PeopleResource {

  private final PersonRepository repository;
  private final PersonService service;
  private final ApplicationEventPublisher eventPublisher;

  public PeopleResource(PersonRepository repository, PersonService service, ApplicationEventPublisher eventPublisher) {
    this.repository = repository;
    this.service = service;
    this.eventPublisher = eventPublisher;
  }

  @GetMapping
  @PreAuthorize("hasAuthority('ROLE_READ_PERSON') and #oauth2.hasScope('read')")
  public Page<Person> search(@QuerydslPredicate(root = Person.class, bindings = DefaultBinder.class) Predicate predicate,
                             Pageable pageable) {
    return repository.findAll(predicate, pageable);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('ROLE_READ_PERSON') and #oauth2.hasScope('read')")
  public ResponseEntity<Person> getById(@PathVariable Long id) {
    return ResponseEntity.of(repository.findById(id));
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ROLE_REGISTER_PERSON') and #oauth2.hasScope('write')")
  public ResponseEntity<Person> create(@Valid @RequestBody Person person, HttpServletResponse response) {
    Person savedPerson = service.save(person);
    eventPublisher.publishEvent(new ResourceCreatedEvent(this, response, savedPerson.getId()));
    return ResponseEntity.status(HttpStatus.CREATED).body(savedPerson);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('ROLE_REGISTER_PERSON') and #oauth2.hasScope('write')")
  public ResponseEntity<Person> update(@PathVariable Long id, @Valid @RequestBody Person person) {
    return ResponseEntity.ok(service.update(id, person));
  }

  @PatchMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasAuthority('ROLE_REGISTER_PERSON') and #oauth2.hasScope('write')")
  public void update(@PathVariable Long id, @RequestBody Boolean active) {
    service.update(id, active);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasAuthority('ROLE_DELETE_PERSON') and #oauth2.hasScope('write')")
  public void delete(@PathVariable Long id) {
    repository.deleteById(id);
  }
}
