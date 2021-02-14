package com.algaworks.algamoney.resource;

import com.algaworks.algamoney.event.ResourceCreatedEvent;
import com.algaworks.algamoney.exception.ApiExceptionHandler;
import com.algaworks.algamoney.exception.NonexistentOrInactivePersonException;
import com.algaworks.algamoney.model.AccountEntry;
import com.algaworks.algamoney.model.projection.EntryStatisticCategory;
import com.algaworks.algamoney.model.projection.EntryStatisticDay;
import com.algaworks.algamoney.model.projection.EntrySummary;
import com.algaworks.algamoney.repository.AccountEntryRepository;
import com.algaworks.algamoney.repository.querydsl.DefaultBinder;
import com.algaworks.algamoney.service.AccountEntryService;
import com.querydsl.core.types.Predicate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/account-entries")
public class AccountEntryResource {
  private final AccountEntryRepository repository;
  private final AccountEntryService service;
  private final ApplicationEventPublisher eventPublisher;

  public AccountEntryResource(AccountEntryRepository repository, AccountEntryService service,
                              ApplicationEventPublisher eventPublisher) {
    this.repository = repository;
    this.service = service;
    this.eventPublisher = eventPublisher;
  }

  @PostMapping("/attachments")
  @PreAuthorize("hasAuthority('ROLE_REGISTER_ACCOUNT_ENTRY') and #oauth2.hasScope('write')")
  public String uploadAttachment(@RequestParam MultipartFile attachment) throws IOException {
    OutputStream out = new FileOutputStream("/home/marcelo/Desktop/anexo--" + attachment.getOriginalFilename());
    out.write(attachment.getBytes());
    out.close();
    return "File uploaded";
  }

  @GetMapping
  @PreAuthorize("hasAuthority('ROLE_READ_ACCOUNT_ENTRY') and #oauth2.hasScope('read')")
  public Page<AccountEntry> search(@QuerydslPredicate(root = AccountEntry.class, bindings = DefaultBinder.class) Predicate predicate,
                                   Pageable pageable) {
    return repository.findAll(predicate, pageable);
  }

  @GetMapping("/summary")
  @PreAuthorize("hasAuthority('ROLE_READ_ACCOUNT_ENTRY') and #oauth2.hasScope('read')")
  public Page<EntrySummary> getSummary(@QuerydslPredicate(root = AccountEntry.class, bindings = DefaultBinder.class) Predicate predicate,
                                       Pageable pageable) {
    return service.getSummary(predicate, pageable);
  }

  @GetMapping("/statistics/by-category")
  @PreAuthorize("hasAuthority('ROLE_READ_ACCOUNT_ENTRY') and #oauth2.hasScope('read')")
  public List<EntryStatisticCategory> totalEntriesByPorCategory() {
    return service.getByCategory(LocalDate.now());
  }

  @GetMapping("/statistics/by-type")
  @PreAuthorize("hasAuthority('ROLE_READ_ACCOUNT_ENTRY') and #oauth2.hasScope('read')")
  public List<EntryStatisticDay> totalEntriesByType() {
    return service.getByType(LocalDate.now());
  }

  @GetMapping("/reports/by-person")
  @PreAuthorize("hasAuthority('ROLE_READ_ACCOUNT_ENTRY') and #oauth2.hasScope('read')")
  public ResponseEntity<byte[]> reportByPerson(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
                                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) throws Exception {
    byte[] report = service.reportByPerson(start, end);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE).body(report);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('ROLE_READ_ACCOUNT_ENTRY') and #oauth2.hasScope('read')")
  public ResponseEntity<AccountEntry> getById(@PathVariable Long id) {
    return ResponseEntity.of(repository.findById(id));
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ROLE_REGISTER_ACCOUNT_ENTRY') and #oauth2.hasScope('write')")
  public ResponseEntity<AccountEntry> create(@Valid @RequestBody AccountEntry accountEntry, HttpServletResponse response) {
    AccountEntry newAccountEntry = service.save(accountEntry);
    eventPublisher.publishEvent(new ResourceCreatedEvent(this, response, newAccountEntry.getId()));
    return ResponseEntity.status(HttpStatus.CREATED).body(newAccountEntry);
  }

  @ExceptionHandler(NonexistentOrInactivePersonException.class)
  public ResponseEntity<Object> handleNonexistentOrInactivePerson(NonexistentOrInactivePersonException ex) {
    String message = "Person nonexistent or inactive";
    String cause = ex.toString();
    List<ApiExceptionHandler.ErrorMessage> errors = Collections.singletonList(new ApiExceptionHandler.ErrorMessage(message, cause));
    return ResponseEntity.badRequest().body(errors);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('ROLE_REGISTER_ACCOUNT_ENTRY') and #oauth2.hasScope('write')")
  public ResponseEntity<AccountEntry> update(@PathVariable Long id, @Valid @RequestBody AccountEntry accountEntry) {
    try {
      AccountEntry accountEntrySalvo = service.update(id, accountEntry);
      return ResponseEntity.ok(accountEntrySalvo);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasAuthority('ROLE_DELETE_ACCOUNT_ENTRY') and #oauth2.hasScope('write')")
  public void delete(@PathVariable Long id) {
    repository.deleteById(id);
  }
}
