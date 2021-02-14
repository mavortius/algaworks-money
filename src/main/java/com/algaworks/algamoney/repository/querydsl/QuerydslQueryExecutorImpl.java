package com.algaworks.algamoney.repository.querydsl;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslJpaPredicateExecutor;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class QuerydslQueryExecutorImpl<T, ID extends Serializable> extends QuerydslJpaPredicateExecutor<T>
        implements QuerydslQueryExecutor<T, ID> {
  private static final EntityPathResolver resolver = SimpleEntityPathResolver.INSTANCE;

  private final Querydsl querydsl;
  private final EntityManager entityManager;

  public QuerydslQueryExecutorImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager, resolver, null);
    EntityPath<T> path = resolver.createPath(entityInformation.getJavaType());
    PathBuilder<T> builder = new PathBuilder<T>(path.getType(), path.getMetadata());
    this.querydsl = new Querydsl(entityManager, builder);
    this.entityManager = entityManager;
  }

  @Override
  public <P> Optional<P> findOne(JPQLQuery<P> query) {
    return Optional.ofNullable(createQuery(query).fetchFirst());
  }

  @Override
  public <P> List<P> findAll(JPQLQuery<P> query) {
    return createQuery(query).fetch();
  }

  @Override
  public <P> Page<P> findAll(JPQLQuery<P> query, Pageable pageable) {
    JPQLQuery<P> attachedQuery = createQuery(query);
    JPQLQuery<P> pagedQuery = querydsl.applyPagination(pageable, attachedQuery);
    return PageableExecutionUtils.getPage(pagedQuery.fetch(), pageable, attachedQuery::fetchCount);
  }

  @Override
  public <P> long count(JPQLQuery<P> query) {
    return createQuery(query).fetchCount();
  }

  @Override
  public <P> boolean exists(JPQLQuery<P> query) {
    return count(query) > 0;
  }

  private <P> JPQLQuery<P> createQuery(JPQLQuery<P> query) {
    return ((JPAQuery<P>) query).clone(Objects.requireNonNull(entityManager));
  }

}
