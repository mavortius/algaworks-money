package com.algaworks.algamoney.repository.query;

import com.algaworks.algamoney.model.AccountEntry;
import com.algaworks.algamoney.model.QAccountEntry;
import com.algaworks.algamoney.model.projection.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.annotations.QueryDelegate;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

public class AccountEntryQueryExpressions extends QuerydslExpressionSupport {
  public static final QAccountEntry ACCOUNT_ENTRY = QAccountEntry.accountEntry;

  public static Predicate accountEntryLike(AccountEntry example) {
    return ACCOUNT_ENTRY.like(example);
  }

  @QueryDelegate(AccountEntry.class)
  public static Predicate like(QAccountEntry root, AccountEntry accountEntry) {
    BooleanBuilder predicate = new BooleanBuilder();

    predicate.and(accountEntry.getId() == null ? null : root.id.eq(accountEntry.getId()));
    predicate.and(accountEntry.getCategory() == null ? null : root.id.eq(accountEntry.getId()));
    predicate.and(accountEntry.getCategory() == null ? null : root.category.like(accountEntry.getCategory()));
    predicate.and(accountEntry.getPerson() == null ? null : root.person.like(accountEntry.getPerson()));
    predicate.and(StringUtils.isEmpty(accountEntry.getDescription()) ? null : root.description.containsIgnoreCase(accountEntry.getDescription()));
    predicate.and(StringUtils.isEmpty(accountEntry.getObservation()) ? null : root.observation.containsIgnoreCase(accountEntry.getObservation()));
    predicate.and(accountEntry.getType() == null ? null : root.type.eq(accountEntry.getType()));
    predicate.and(accountEntry.getValue() == null ? null : root.value.eq(accountEntry.getValue()));

    if (accountEntry.getPaymentDateFrom() != null && accountEntry.getPaymentDateTo() != null) {
      predicate.and(root.paymentDate.between(accountEntry.getPaymentDateFrom(), accountEntry.getPaymentDateTo()));
    } else {
      predicate.and(accountEntry.getPaymentDate() == null ? null : root.paymentDate.eq(accountEntry.getPaymentDate()));
    }

    if (accountEntry.getDueDateFrom() != null && accountEntry.getDueDateTo() != null) {
      predicate.and(root.dueDate.between(accountEntry.getDueDateFrom(), accountEntry.getDueDateTo()));
    } else {
      predicate.and(accountEntry.getDueDate() == null ? null : root.dueDate.eq(accountEntry.getDueDate()));
    }

    return predicate.getValue();
  }

  public static JPQLQuery<EntrySummary> entrySummary(Predicate predicate) {
    return select(new QEntrySummary(ACCOUNT_ENTRY)).from(ACCOUNT_ENTRY)
            .innerJoin(ACCOUNT_ENTRY.category).fetchJoin()
            .innerJoin(ACCOUNT_ENTRY.person).fetchJoin()
            .where(predicate);
  }

  public static JPQLQuery<EntrySummary> expiredEntrySummary() {
    return entrySummary(ACCOUNT_ENTRY.paymentDate.isNull().and(ACCOUNT_ENTRY.dueDate.loe(LocalDate.now())));
  }

  public static JPQLQuery<EntryStatisticCategory> entryStatisticCategory(LocalDate referenceMonth) {
    LocalDate firstDay = referenceMonth.withDayOfMonth(1);
    LocalDate lastDay = referenceMonth.withDayOfMonth(referenceMonth.lengthOfMonth());
    Predicate period = ACCOUNT_ENTRY.dueDate.between(firstDay, lastDay);
    return select(new QEntryStatisticCategory(ACCOUNT_ENTRY.category, ACCOUNT_ENTRY.value.sum()))
            .from(ACCOUNT_ENTRY)
            .innerJoin(ACCOUNT_ENTRY.category).fetchJoin()
            .innerJoin(ACCOUNT_ENTRY.person).fetchJoin()
            .where(period);
  }

  public static JPQLQuery<EntryStatisticDay> entryStatisticDay(LocalDate referenceMonth) {
    LocalDate firstDay = referenceMonth.withDayOfMonth(1);
    LocalDate lastDay = referenceMonth.withDayOfMonth(referenceMonth.lengthOfMonth());
    Predicate period = ACCOUNT_ENTRY.dueDate.between(firstDay, lastDay);
    return select(new QEntryStatisticDay(ACCOUNT_ENTRY.type, ACCOUNT_ENTRY.dueDate, ACCOUNT_ENTRY.value.sum()))
            .from(ACCOUNT_ENTRY)
            .innerJoin(ACCOUNT_ENTRY.category).fetchJoin()
            .innerJoin(ACCOUNT_ENTRY.person).fetchJoin()
            .where(period);
  }

  public static JPQLQuery<EntryStatisticPerson> entryStatisticPerson(LocalDate startDate, LocalDate endDate) {
    Predicate period = ACCOUNT_ENTRY.dueDate.between(startDate, endDate);
    return select(new QEntryStatisticPerson(ACCOUNT_ENTRY.type, ACCOUNT_ENTRY.person, ACCOUNT_ENTRY.value.sum()))
            .from(ACCOUNT_ENTRY)
            .innerJoin(ACCOUNT_ENTRY.category).fetchJoin()
            .innerJoin(ACCOUNT_ENTRY.person).fetchJoin()
            .where(period);
  }
}
