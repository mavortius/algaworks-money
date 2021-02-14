package com.algaworks.algamoney.repository.querydsl;

import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.lang.NonNull;

import javax.persistence.EntityManager;

public class QuerydslRepositoryFactoryBean<T extends Repository<S, I>, S, I> extends JpaRepositoryFactoryBean<T, S, I> {

  public QuerydslRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
    super(repositoryInterface);
  }

  @NonNull
  @Override
  protected RepositoryFactorySupport createRepositoryFactory(@NonNull EntityManager entityManager) {
    return new QuerydslRepositoryFactory(entityManager);
  }
}

