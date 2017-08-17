package com.algaworks.algaworksmoney.repository;

import com.algaworks.algaworksmoney.model.Lancamento;
import com.algaworks.algaworksmoney.model.QLancamento;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.stereotype.Repository;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long>,
        QueryDslPredicateExecutor<Lancamento>,
        QuerydslBinderCustomizer<QLancamento> {

    @Override
    default void customize(QuerydslBindings bindings, QLancamento lancamento) {
        bindings.bind(lancamento.dataVencimentoDe).first((path, value) -> lancamento.dataVencimento.goe(value));
        bindings.bind(lancamento.dataVencimentoAte).first((path, value) -> lancamento.dataVencimento.loe(value));
        bindings.bind(lancamento.dataPagamentoDe).first((path, value) -> lancamento.dataPagamento.goe(value));
        bindings.bind(lancamento.dataPagamentoAte).first((path, value) -> lancamento.dataPagamento.loe(value));
        bindings.bind(String.class).first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
    }
}
