package com.algaworks.algamoney.model.projection;

import com.algaworks.algamoney.model.Person;
import com.algaworks.algamoney.model.EntryType;
import com.querydsl.core.annotations.QueryProjection;

import java.math.BigDecimal;

public class EntryStatisticPerson {
  private EntryType type;
  private Person person;
  private BigDecimal total;

  @QueryProjection
  public EntryStatisticPerson(EntryType type, Person person, BigDecimal total) {
    this.type = type;
    this.person = person;
    this.total = total;
  }

  public EntryType getType() {
    return type;
  }

  public void setType(EntryType type) {
    this.type = type;
  }

  public Person getPessoa() {
    return person;
  }

  public void setPessoa(Person person) {
    this.person = person;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }
}
