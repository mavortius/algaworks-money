package com.algaworks.algamoney.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "account_entries")
public class AccountEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Size(min = 3, max = 50)
  private String description;

  @NotNull
  private LocalDate dueDate;

  private LocalDate paymentDate;

  @NotNull
  private BigDecimal value;

  private String observation;

  @NotNull
  @Enumerated(EnumType.STRING)
  private EntryType type;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  private Category category;

  @JsonIgnoreProperties("contacts")
  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  private Person person;

  @Transient
  @JsonIgnore
  private LocalDate paymentDateFrom;

  @Transient
  @JsonIgnore
  private LocalDate paymentDateTo;

  @Transient
  @JsonIgnore
  private LocalDate dueDateFrom;

  @Transient
  @JsonIgnore
  private LocalDate dueDateTo;

  private String attachment;

  @Transient
  private String urlAttachment;
  @JsonIgnore
  public boolean isReceita() {
    return type.equals(EntryType.INCOME);
  }

  public LocalDate getPaymentDateFrom() {
    return paymentDateFrom;
  }

  public LocalDate getPaymentDateTo() {
    return paymentDateTo;
  }

  public LocalDate getDueDateFrom() {
    return dueDateFrom;
  }

  public LocalDate getDueDateTo() {
    return dueDateTo;
  }

  public void setPaymentDateFrom(LocalDate paymentDateFrom) {
    this.paymentDateFrom = paymentDateFrom;
  }

  public void setPaymentDateTo(LocalDate paymentDateTo) {
    this.paymentDateTo = paymentDateTo;
  }

  public void setDueDateFrom(LocalDate dueDateFrom) {
    this.dueDateFrom = dueDateFrom;
  }

  public void setDueDateTo(LocalDate dueDateTo) {
    this.dueDateTo = dueDateTo;
  }

  public String getAttachment() {
    return attachment;
  }

  public void setAttachment(String anexo) {
    this.attachment = anexo;
  }

  public String getUrlAttachment() {
    return urlAttachment;
  }

  public void setUrlAttachment(String urlAttachment) {
    this.urlAttachment = urlAttachment;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDate getDueDate() {
    return dueDate;
  }

  public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
  }

  public LocalDate getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(LocalDate paymentDate) {
    this.paymentDate = paymentDate;
  }

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }

  public String getObservation() {
    return observation;
  }

  public void setObservation(String observation) {
    this.observation = observation;
  }

  public EntryType getType() {
    return type;
  }

  public void setType(EntryType type) {
    this.type = type;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AccountEntry)) return false;

    AccountEntry that = (AccountEntry) o;

    if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
    if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
      return false;
    if (getDueDate() != null ? !getDueDate().equals(that.getDueDate()) : that.getDueDate() != null) return false;
    if (getPaymentDate() != null ? !getPaymentDate().equals(that.getPaymentDate()) : that.getPaymentDate() != null)
      return false;
    if (getValue() != null ? !getValue().equals(that.getValue()) : that.getValue() != null) return false;
    if (getObservation() != null ? !getObservation().equals(that.getObservation()) : that.getObservation() != null)
      return false;
    if (getType() != that.getType()) return false;
    if (getCategory() != null ? !getCategory().equals(that.getCategory()) : that.getCategory() != null) return false;
    if (getPerson() != null ? !getPerson().equals(that.getPerson()) : that.getPerson() != null) return false;
    if (getPaymentDateFrom() != null ? !getPaymentDateFrom().equals(that.getPaymentDateFrom()) : that.getPaymentDateFrom() != null)
      return false;
    if (getPaymentDateTo() != null ? !getPaymentDateTo().equals(that.getPaymentDateTo()) : that.getPaymentDateTo() != null)
      return false;
    if (getDueDateFrom() != null ? !getDueDateFrom().equals(that.getDueDateFrom()) : that.getDueDateFrom() != null)
      return false;
    if (getDueDateTo() != null ? !getDueDateTo().equals(that.getDueDateTo()) : that.getDueDateTo() != null)
      return false;
    if (getAttachment() != null ? !getAttachment().equals(that.getAttachment()) : that.getAttachment() != null)
      return false;
    return getUrlAttachment() != null ? getUrlAttachment().equals(that.getUrlAttachment()) : that.getUrlAttachment() == null;
  }

  @Override
  public int hashCode() {
    int result = getId() != null ? getId().hashCode() : 0;
    result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
    result = 31 * result + (getDueDate() != null ? getDueDate().hashCode() : 0);
    result = 31 * result + (getPaymentDate() != null ? getPaymentDate().hashCode() : 0);
    result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
    result = 31 * result + (getObservation() != null ? getObservation().hashCode() : 0);
    result = 31 * result + (getType() != null ? getType().hashCode() : 0);
    result = 31 * result + (getCategory() != null ? getCategory().hashCode() : 0);
    result = 31 * result + (getPerson() != null ? getPerson().hashCode() : 0);
    result = 31 * result + (getPaymentDateFrom() != null ? getPaymentDateFrom().hashCode() : 0);
    result = 31 * result + (getPaymentDateTo() != null ? getPaymentDateTo().hashCode() : 0);
    result = 31 * result + (getDueDateFrom() != null ? getDueDateFrom().hashCode() : 0);
    result = 31 * result + (getDueDateTo() != null ? getDueDateTo().hashCode() : 0);
    result = 31 * result + (getAttachment() != null ? getAttachment().hashCode() : 0);
    result = 31 * result + (getUrlAttachment() != null ? getUrlAttachment().hashCode() : 0);
    return result;
  }
}
