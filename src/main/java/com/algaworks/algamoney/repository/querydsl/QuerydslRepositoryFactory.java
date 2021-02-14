package com.algaworks.algamoney.repository.querydsl;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryComposition;
import org.springframework.data.repository.core.support.RepositoryFragment;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class QuerydslRepositoryFactory extends JpaRepositoryFactory {
  private final EntityManager entityManager;

  public QuerydslRepositoryFactory(EntityManager entityManager) {
    super(entityManager);
    this.entityManager = entityManager;
  }

  @Override
  protected RepositoryComposition.RepositoryFragments getRepositoryFragments(RepositoryMetadata metadata) {
    RepositoryComposition.RepositoryFragments fragments = super.getRepositoryFragments(metadata);

    if (QuerydslQueryExecutor.class.isAssignableFrom(metadata.getRepositoryInterface())) {
      JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(metadata.getDomainType());
      Object querydslFragment = getTargetRepositoryViaReflection(QuerydslQueryExecutorImpl.class, entityInformation, entityManager);
      fragments = fragments.append(RepositoryFragment.implemented(querydslFragment));
    }

    return fragments;
  }

}
