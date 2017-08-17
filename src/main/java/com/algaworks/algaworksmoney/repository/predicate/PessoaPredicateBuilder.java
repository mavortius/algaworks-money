package com.algaworks.algaworksmoney.repository.predicate;

import com.algaworks.algaworksmoney.model.Pessoa;
import com.algaworks.algaworksmoney.model.QPessoa;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.annotations.QueryDelegate;
import com.querydsl.core.types.Predicate;
import org.springframework.util.StringUtils;

public class PessoaPredicateBuilder {

    @QueryDelegate(Pessoa.class)
    public static Predicate like(QPessoa qPessoa, Pessoa pessoa) {
        BooleanBuilder predicate = new BooleanBuilder();

        predicate.and(pessoa.getCodigo() == null ? null : qPessoa.codigo.eq(pessoa.getCodigo()));
        predicate.and(StringUtils.isEmpty(pessoa.getNome()) ? null : qPessoa.nome.containsIgnoreCase(pessoa.getNome()));

        return predicate.getValue();
    }
}
