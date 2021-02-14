package com.algaworks.algamoney.repository.query;

import com.algaworks.algamoney.model.Person;
import com.algaworks.algamoney.model.QPerson;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.annotations.QueryDelegate;
import com.querydsl.core.types.Predicate;
import org.springframework.util.StringUtils;

public class PersonQueryExpressions extends QuerydslExpressionSupport {
  @QueryDelegate(Person.class)
  public static Predicate like(QPerson root, Person person) {
    BooleanBuilder predicate = new BooleanBuilder();
    predicate.and(person.getId() == null ? null : root.id.eq(person.getId()));
    predicate.and(StringUtils.isEmpty(person.getName()) ? null : root.name.containsIgnoreCase(person.getName()));
    return predicate.getValue();
  }
}
