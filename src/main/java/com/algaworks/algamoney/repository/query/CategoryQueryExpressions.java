package com.algaworks.algamoney.repository.query;

import com.algaworks.algamoney.model.Category;
import com.algaworks.algamoney.model.QCategory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.annotations.QueryDelegate;
import com.querydsl.core.types.Predicate;
import org.springframework.util.StringUtils;

public class CategoryQueryExpressions extends QuerydslExpressionSupport {
  @QueryDelegate(Category.class)
  public static Predicate like(QCategory root, Category category) {
    BooleanBuilder predicate = new BooleanBuilder();
    predicate.and(category.getId() == null ? null : root.id.eq(category.getId()));
    predicate.and(StringUtils.isEmpty(category.getName()) ? null : root.name.containsIgnoreCase(category.getName()));
    return predicate.getValue();
  }
}
