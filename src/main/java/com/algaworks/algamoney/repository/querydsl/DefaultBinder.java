package com.algaworks.algamoney.repository.querydsl;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class DefaultBinder<Q extends EntityPath<?>> implements QuerydslBinderCustomizer<Q> {

  @Override
  public void customize(QuerydslBindings bindings, Q root) {
    bindings.bind(String.class).first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
    bindings.bind(LocalDate.class).all(this::betweenDates);
  }

  private Optional<Predicate> betweenDates(DatePath<LocalDate> datePath, Collection<? extends LocalDate> values) {
    List<LocalDate> dates = new ArrayList<>(values);

    if (dates.isEmpty()) {
      return Optional.empty();
    }

    if (dates.size() == 1) {
      return Optional.ofNullable(datePath.eq(dates.get(0)));
    } else {
      LocalDate from = dates.get(0);
      LocalDate to = dates.get(1);
      return Optional.of(datePath.between(from, to));
    }
  }
}
