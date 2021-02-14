package com.algaworks.algamoney.repository;

import com.algaworks.algamoney.model.City;
import com.algaworks.algamoney.repository.querydsl.QuerydslRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends QuerydslRepository<City, Long> {
}
