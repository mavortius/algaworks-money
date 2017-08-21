package com.algaworks.algaworksmoney.repository.predicate;

import com.algaworks.algaworksmoney.model.Lancamento;
import com.algaworks.algaworksmoney.model.QLancamento;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.annotations.QueryDelegate;
import com.querydsl.core.types.Predicate;
import org.springframework.util.StringUtils;

import static com.algaworks.algaworksmoney.model.QLancamento.lancamento;

public class LancamentoPredicateBuilder {

    public static Predicate LancamentoLike(Lancamento exemplo) {
        return lancamento.like(exemplo);
    }

    @QueryDelegate(Lancamento.class)
    public static Predicate like(QLancamento qLancamento, Lancamento lancamento) {
        BooleanBuilder predicate = new BooleanBuilder();

        predicate.and(lancamento.getCodigo() == null ? null : qLancamento.codigo.eq(lancamento.getCodigo()));
        predicate.and(lancamento.getCategoria() == null ? null : qLancamento.codigo.eq(lancamento.getCodigo()));
        predicate.and(lancamento.getCategoria() == null ? null : qLancamento.categoria.like(lancamento.getCategoria()));
        predicate.and(lancamento.getPessoa() == null ? null : qLancamento.pessoa.like(lancamento.getPessoa()));
        predicate.and(lancamento.getDataPagamento() == null ? null : qLancamento.dataPagamento.eq(lancamento.getDataPagamento()));
        predicate.and(lancamento.getDataVencimento() == null ? null : qLancamento.dataVencimento.eq(lancamento.getDataVencimento()));
        predicate.and(StringUtils.isEmpty(lancamento.getDescricao()) ? null : qLancamento.descricao.containsIgnoreCase(lancamento.getDescricao()));
        predicate.and(StringUtils.isEmpty(lancamento.getObservacao()) ? null : qLancamento.observacao.containsIgnoreCase(lancamento.getObservacao()));
        predicate.and(lancamento.getTipo() == null ? null : qLancamento.tipo.eq(lancamento.getTipo()));
        predicate.and(lancamento.getValor() == null ? null : qLancamento.valor.eq(lancamento.getValor()));

        return predicate.getValue();
    }
}
