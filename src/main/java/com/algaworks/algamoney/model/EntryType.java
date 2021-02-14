package com.algaworks.algamoney.model;

public enum EntryType {
  INCOME("Receita"),
  EXPENSE("Despesa");

  private final String description;

  EntryType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
