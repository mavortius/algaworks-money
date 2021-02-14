package com.algaworks.algamoney.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "contacts")
public class Contact {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty
  private String name;

  @Email
  @NotNull
  private String email;

  @NotEmpty
  private String telephone;

  @ManyToOne(fetch = FetchType.LAZY)
  private Person person;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public Person getPessoa() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Contact contact = (Contact) o;
    return Objects.equals(id, contact.id) &&
            Objects.equals(name, contact.name) &&
            Objects.equals(email, contact.email) &&
            Objects.equals(telephone, contact.telephone) &&
            Objects.equals(person, contact.person);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, email, telephone, person);
  }
}
