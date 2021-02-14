package com.algaworks.algamoney.repository.query;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;

public class QuerydslExpressionSupport {

  public static Expression<Boolean> caseWhen(BooleanExpression when, boolean then, Expression<Boolean> otherwise) {
    return new CaseBuilder().when(when).then(then).otherwise(otherwise);
  }

  public static Expression<String> caseWhen(BooleanExpression when, StringPath then, String otherwise) {
    return new CaseBuilder().when(when).then(then).otherwise(otherwise);
  }

  public static <T> JPQLQuery<T> select(Expression<T> expr) {
    return new JPAQuery<>().select(expr);
  }

  public static JPAQuery<?> select(Expression<?>... exprs) {
    return new JPAQuery<Void>().select(exprs);
  }

  public static <T> JPQLQuery<T> selectFrom(EntityPath<T> entityPath) {
    return new JPAQuery<>().select(entityPath).from(entityPath);
  }

  public static <P> JPQLQuery<P> selectAsSubQuery(Expression<P> projection) {
    return JPAExpressions.select(projection);
  }

}
