package com.algaworks.algamoney.service;

import com.algaworks.algamoney.exception.NonexistentOrInactivePersonException;
import com.algaworks.algamoney.mail.Mailer;
import com.algaworks.algamoney.model.AccountEntry;
import com.algaworks.algamoney.model.Person;
import com.algaworks.algamoney.model.User;
import com.algaworks.algamoney.model.projection.EntryStatisticCategory;
import com.algaworks.algamoney.model.projection.EntryStatisticDay;
import com.algaworks.algamoney.model.projection.EntryStatisticPerson;
import com.algaworks.algamoney.model.projection.EntrySummary;
import com.algaworks.algamoney.repository.AccountEntryRepository;
import com.algaworks.algamoney.repository.PersonRepository;
import com.algaworks.algamoney.repository.UserRepository;
import com.google.common.collect.Lists;
import com.querydsl.core.types.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.algaworks.algamoney.repository.query.AccountEntryQueryExpressions.*;
import static com.algaworks.algamoney.repository.query.UserQueryExpressions.userByPermissionDescription;

@Service
public class AccountEntryService {
  private static final Logger logger = LoggerFactory.getLogger(AccountEntryService.class);

  private static final String RECIPIENTS = "ROLE_SEARCH_ENTRY";
  private static final String MAIL_EXPIRED_ENTRIES_NOTIFICATION_TEMPLATE = "mail/expired-entries-notification";

  private final AccountEntryRepository repository;
  private final PersonRepository personRepository;
  private final UserRepository userRepository;
  private final Mailer mailer;

  public AccountEntryService(AccountEntryRepository repository,
                             PersonRepository personRepository,
                             UserRepository userRepository,
                             Mailer mailer) {
    this.repository = repository;
    this.personRepository = personRepository;
    this.userRepository = userRepository;
    this.mailer = mailer;
  }

  @Scheduled(cron = "0 0 6 * * *")
  public void notifyExpiredEntries() {
    if (logger.isDebugEnabled()) {
      logger.debug("Preparing to send expired entries notification mails...");
    }

    List<EntrySummary> expiredEntries = repository.findAll(expiredEntrySummary());

    if (expiredEntries.isEmpty()) {
      logger.info("No expired entries to notify.");
      return;
    }

    logger.info("There are {} expired entries.", expiredEntries.size());

    List<User> recipients = Lists.newArrayList(userRepository.findAll(userByPermissionDescription(RECIPIENTS)));

    if (recipients.isEmpty()) {
      logger.warn("There are expiredEntries entries, but the system did not found recipients.");
      return;
    }

    Map<String, Object> values = new HashMap<>();
    values.put("entries", expiredEntries);
    List<String> emails = recipients.stream()
            .map(User::getEmail)
            .collect(Collectors.toList());

    mailer.send("avisos@algamoney.com", emails, "Lançamentos expiredEntries",
            MAIL_EXPIRED_ENTRIES_NOTIFICATION_TEMPLATE, values);

    logger.info("Notification email sending finished.");
  }

  public Page<EntrySummary> getSummary(Predicate predicate, Pageable pageable) {
    return repository.findAll(entrySummary(predicate), pageable);
  }

  public List<EntryStatisticCategory> getByCategory(LocalDate referenceMonth) {
    return repository.findAll(entryStatisticCategory(referenceMonth));
  }

  public List<EntryStatisticDay> getByType(LocalDate referenceMonth) {
    return repository.findAll(entryStatisticDay(referenceMonth));
  }

  public List<EntryStatisticPerson> getByPerson(LocalDate startDate, LocalDate endDate) {
    return repository.findAll(entryStatisticPerson(startDate, endDate));
  }

  // TODO: implement
  public byte[] reportByPerson(LocalDate startDate, LocalDate endDate) throws Exception {
    List<EntryStatisticPerson> data = getByPerson(startDate, endDate);
    Map<String, Object> params = new HashMap<>();

    params.put("DT_INICIO", Date.valueOf(startDate));
    params.put("DT_FIM", Date.valueOf(endDate));
    params.put("REPORT_LOCALE", LocaleContextHolder.getLocale());

    InputStream inputStream = this.getClass().getResourceAsStream(
            "/relatorios/lancamentos-por-pessoa.jasper");
    // JasperPrint print = JasperFillManager.fillReport(inputStream, params, new JRBeanCollectionDataSource(data));

    return null; // JasperExportManager.exportReportToPdf(print);
  }

  public AccountEntry save(AccountEntry accountEntry) {
    validatePerson(accountEntry);
    return repository.save(accountEntry);
  }

  public AccountEntry update(Long id, AccountEntry accountEntry) {
    AccountEntry savedAccountEntry = findEntry(id);

    if (!accountEntry.getPerson().equals(savedAccountEntry.getPerson())) {
      validatePerson(accountEntry);
    }

    BeanUtils.copyProperties(accountEntry, savedAccountEntry, "id");

    return repository.save(savedAccountEntry);
  }

  private void validatePerson(AccountEntry accountEntry) {
    if (accountEntry.getPerson().getId() != null) {
      Person person = personRepository.findById(accountEntry.getPerson().getId())
              .orElseThrow(NonexistentOrInactivePersonException::new);
      if (!person.isActive()) {
        throw new NonexistentOrInactivePersonException();
      }
    }
  }

  private AccountEntry findEntry(Long id) {
    return repository.findById(id).orElseThrow(IllegalArgumentException::new);
  }
}
