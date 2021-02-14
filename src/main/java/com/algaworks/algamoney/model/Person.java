package com.algaworks.algamoney.model;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "people")
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String name;

  @Embedded
  private Address address;

  @NotNull
  private Boolean active;

  @Valid
  @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Contact> contacts;

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

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public List<Contact> getContacts() {
    return contacts;
  }

  public void setContacts(List<Contact> contacts) {
    this.contacts = contacts;
  }

  public boolean isActive() {
    return active;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Person)) return false;

    Person person = (Person) o;

    if (getId() != null ? !getId().equals(person.getId()) : person.getId() != null) return false;
    if (getName() != null ? !getName().equals(person.getName()) : person.getName() != null) return false;
    if (getAddress() != null ? !getAddress().equals(person.getAddress()) : person.getAddress() != null) return false;
    if (getActive() != null ? !getActive().equals(person.getActive()) : person.getActive() != null) return false;
    return getContacts() != null ? getContacts().equals(person.getContacts()) : person.getContacts() == null;
  }

  @Override
  public int hashCode() {
    int result = getId() != null ? getId().hashCode() : 0;
    result = 31 * result + (getName() != null ? getName().hashCode() : 0);
    result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
    result = 31 * result + (getActive() != null ? getActive().hashCode() : 0);
    result = 31 * result + (getContacts() != null ? getContacts().hashCode() : 0);
    return result;
  }
}
