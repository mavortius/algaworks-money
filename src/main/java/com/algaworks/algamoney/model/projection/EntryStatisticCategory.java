package com.algaworks.algamoney.model.projection;

import com.algaworks.algamoney.model.Category;
import com.querydsl.core.annotations.QueryProjection;

import java.math.BigDecimal;

public class EntryStatisticCategory {
  private Category category;
  private BigDecimal total;

  @QueryProjection
  public EntryStatisticCategory(Category category, BigDecimal total) {
    this.category = category;
    this.total = total;
  }

  public Category getCategoria() {
    return category;
  }

  public void setCategoria(Category category) {
    this.category = category;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }
}
