package com.algaworks.algamoney.repository.querydsl;

import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface QuerydslQueryExecutor<T, ID extends Serializable> extends Repository<T, ID> {
  <P> Optional<P> findOne(JPQLQuery<P> query);

  <P> List<P> findAll(JPQLQuery<P> query);

  <P> Page<P> findAll(JPQLQuery<P> query, Pageable pageable);

  <P> long count(JPQLQuery<P> query);

  <P> boolean exists(JPQLQuery<P> query);
}
