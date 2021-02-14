package com.algaworks.algamoney.repository.querydsl;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface QuerydslRepository<T, ID extends Serializable> extends CrudRepository<T, ID>,
        QuerydslQueryExecutor<T, ID>, QuerydslPredicateExecutor<T> {
}
