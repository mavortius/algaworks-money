package com.algaworks.algamoney.resource;

import com.algaworks.algamoney.event.ResourceCreatedEvent;
import com.algaworks.algamoney.model.Category;
import com.algaworks.algamoney.repository.CategoryRepository;
import com.google.common.collect.Lists;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryResource {

  private final CategoryRepository repository;
  private final ApplicationEventPublisher eventPublisher;

  public CategoryResource(CategoryRepository repository, ApplicationEventPublisher eventPublisher) {
    this.repository = repository;
    this.eventPublisher = eventPublisher;
  }

  @GetMapping
  @PreAuthorize("hasAuthority('ROLE_READ_CATEGORY') and #oauth2.hasScope('read')")
  public List<Category> list() {
    return Lists.newArrayList(repository.findAll());
  }

  @PostMapping
  @PreAuthorize("hasAnyAuthority('ROLE_REGISTER_CATEGORY') and #oauth2.hasScope('write')")
  public ResponseEntity<Category> create(@Valid @RequestBody Category category, HttpServletResponse response) {
    Category newCategory = repository.save(category);
    eventPublisher.publishEvent(new ResourceCreatedEvent(this, response, newCategory.getId()));
    return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('ROLE_READ_CATEGORY') and #oauth2.hasScope('read')")
  public ResponseEntity<Category> getById(@PathVariable Long id) {
    return ResponseEntity.of(repository.findById(id));
  }
}

