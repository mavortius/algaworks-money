package com.algaworks.algaworksmoney.repository.predicate;

import com.algaworks.algaworksmoney.model.Lancamento;
import com.algaworks.algaworksmoney.model.QLancamento;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.annotations.QueryDelegate;
import com.querydsl.core.types.Predicate;
import org.springframework.util.StringUtils;

import static com.algaworks.algaworksmoney.model.QLancamento.lancamento;

public class LancamentoPredicateBuilder {

    public static Predicate lancamentoLike(Lancamento exemplo) {
        return lancamento.like(exemplo);
    }

    @QueryDelegate(Lancamento.class)
    public static Predicate like(QLancamento root, Lancamento lancamento) {
        BooleanBuilder predicate = new BooleanBuilder();

        predicate.and(lancamento.getCodigo() == null ? null : root.codigo.eq(lancamento.getCodigo()));
        predicate.and(lancamento.getCategoria() == null ? null : root.codigo.eq(lancamento.getCodigo()));
        predicate.and(lancamento.getCategoria() == null ? null : root.categoria.like(lancamento.getCategoria()));
        predicate.and(lancamento.getPessoa() == null ? null : root.pessoa.like(lancamento.getPessoa()));
        predicate.and(StringUtils.isEmpty(lancamento.getDescricao()) ? null : root.descricao.containsIgnoreCase(lancamento.getDescricao()));
        predicate.and(StringUtils.isEmpty(lancamento.getObservacao()) ? null : root.observacao.containsIgnoreCase(lancamento.getObservacao()));
        predicate.and(lancamento.getTipo() == null ? null : root.tipo.eq(lancamento.getTipo()));
        predicate.and(lancamento.getValor() == null ? null : root.valor.eq(lancamento.getValor()));

        if(lancamento.getDataPagamentoDe() != null && lancamento.getDataPagamentoAte() != null)  {
            predicate.and(root.dataPagamento.between(lancamento.getDataPagamentoDe(), lancamento.getDataPagamentoAte()));
        } else {
            predicate.and(lancamento.getDataPagamento() == null ? null : root.dataPagamento.eq(lancamento.getDataPagamento()));
        }

        if(lancamento.getDataVencimentoDe() != null && lancamento.getDataVencimentoAte() != null) {
            predicate.and(root.dataVencimento.between(lancamento.getDataVencimentoDe(), lancamento.getDataVencimentoAte()));
        } else {
            predicate.and(lancamento.getDataVencimento() == null ? null : root.dataVencimento.eq(lancamento.getDataVencimento()));
        }

        return predicate.getValue();
    }
}
