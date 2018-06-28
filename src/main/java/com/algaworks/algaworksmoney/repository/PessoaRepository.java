package com.algaworks.algaworksmoney.repository;

import com.algaworks.algaworksmoney.model.Pessoa;
import com.algaworks.algaworksmoney.model.QPessoa;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long>,
                                            QuerydslPredicateExecutor<Pessoa>,
                                            QuerydslBinderCustomizer<QPessoa> {
    @Override
    default void customize(QuerydslBindings bindings, QPessoa lancamento) {
        bindings.bind(String.class).first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
    }
}
