package com.algaworks.algamoney.repository;

import com.algaworks.algamoney.model.User;
import com.algaworks.algamoney.repository.querydsl.QuerydslRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends QuerydslRepository<User, Long> {
}
