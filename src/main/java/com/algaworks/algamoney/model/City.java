package com.algaworks.algamoney.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cities")
public class City {

  @Id
  private Long id;

  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  private State state;

  public Long getId() {
    return id;
  }

  public void setId(Long codigo) {
    this.id = codigo;
  }

  public String getName() {
    return name;
  }

  public void setName(String nome) {
    this.name = nome;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    City city = (City) o;
    return Objects.equals(name, city.name) &&
            Objects.equals(state, city.state);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, state);
  }
}
