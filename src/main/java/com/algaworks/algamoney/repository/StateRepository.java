package com.algaworks.algamoney.repository;

import com.algaworks.algamoney.model.State;
import com.algaworks.algamoney.repository.querydsl.QuerydslRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends QuerydslRepository<State, Long> {
}
