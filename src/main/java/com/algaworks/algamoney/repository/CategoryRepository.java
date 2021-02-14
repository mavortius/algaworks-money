package com.algaworks.algamoney.repository;

import com.algaworks.algamoney.model.Category;
import com.algaworks.algamoney.repository.querydsl.QuerydslRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends QuerydslRepository<Category, Long> {
}
