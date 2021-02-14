package com.algaworks.algamoney.repository;

import com.algaworks.algamoney.model.Person;
import com.algaworks.algamoney.repository.querydsl.QuerydslRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends QuerydslRepository<Person, Long> {
}
