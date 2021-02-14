package com.algaworks.algamoney.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "permissions")
public class Permission {

  @Id
  private Long id;
  private String description;

  public Long getId() {
    return id;
  }

  public void setId(Long codigo) {
    this.id = codigo;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String descricao) {
    this.description = descricao;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Permission permission = (Permission) o;
    return Objects.equals(id, permission.id);
  }

  @Override
  public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }
}
