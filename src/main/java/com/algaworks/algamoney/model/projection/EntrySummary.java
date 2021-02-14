package com.algaworks.algamoney.model.projection;

import com.algaworks.algamoney.model.AccountEntry;
import com.algaworks.algamoney.model.EntryType;
import com.querydsl.core.annotations.QueryProjection;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EntrySummary {
  private final Long id;
  private final String description;
  private final LocalDate dueDate;
  private final LocalDate paymentDate;
  private final BigDecimal value;
  private final EntryType type;
  private final String category;
  private final String person;

  @QueryProjection
  public EntrySummary(AccountEntry entry) {
    this.id = entry.getId();
    this.description = entry.getDescription();
    this.dueDate = entry.getDueDate();
    this.paymentDate = entry.getPaymentDate();
    this.value = entry.getValue();
    this.type = entry.getType();
    this.category = entry.getCategory().getName();
    this.person = entry.getPerson().getName();
  }

  public Long getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public LocalDate getPaymentDate() {
    return paymentDate;
  }

  public BigDecimal getValue() {
    return value;
  }

  public EntryType getType() {
    return type;
  }

  public String getCategory() {
    return category;
  }

  public String getPerson() {
    return person;
  }

}
