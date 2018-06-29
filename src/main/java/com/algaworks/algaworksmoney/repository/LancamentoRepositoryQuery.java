package com.algaworks.algaworksmoney.repository;

import com.algaworks.algaworksmoney.model.Lancamento;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LancamentoRepositoryQuery extends QueryDslRepository<Lancamento, Long> {

    public LancamentoRepositoryQuery() {
        super(Lancamento.class);
    }

    // TODO: Fixme
    @Override
    public Optional<Lancamento> findOne(Predicate predicate) {
        return Optional.empty();
    }
}
