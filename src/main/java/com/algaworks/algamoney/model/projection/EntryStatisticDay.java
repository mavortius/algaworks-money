package com.algaworks.algamoney.model.projection;

import com.algaworks.algamoney.model.EntryType;
import com.querydsl.core.annotations.QueryProjection;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EntryStatisticDay {
  private EntryType type;
  private LocalDate day;
  private BigDecimal total;

  @QueryProjection
  public EntryStatisticDay(EntryType type, LocalDate day, BigDecimal total) {
    this.type = type;
    this.day = day;
    this.total = total;
  }

  public EntryType getType() {
    return type;
  }

  public void setType(EntryType type) {
    this.type = type;
  }

  public LocalDate getDay() {
    return day;
  }

  public void setDay(LocalDate day) {
    this.day = day;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }
}
