package com.algaworks.algaworksmoney.repository;

import com.algaworks.algaworksmoney.model.Lancamento;
import org.springframework.stereotype.Repository;

@Repository
public class LancamentoRepositoryQuery extends QueryDslRepository<Lancamento, Long> {

    public LancamentoRepositoryQuery() {
        super(Lancamento.class);
    }
}
