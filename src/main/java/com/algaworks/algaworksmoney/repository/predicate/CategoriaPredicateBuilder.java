package com.algaworks.algaworksmoney.repository.predicate;

import com.algaworks.algaworksmoney.model.Categoria;
import com.algaworks.algaworksmoney.model.QCategoria;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.annotations.QueryDelegate;
import com.querydsl.core.types.Predicate;
import org.springframework.util.StringUtils;

public class CategoriaPredicateBuilder {

    @QueryDelegate(Categoria.class)
    public static Predicate like(QCategoria qCategoria, Categoria categoria) {
        BooleanBuilder predicate = new BooleanBuilder();

        predicate.and(categoria.getCodigo() == null ? null : qCategoria.codigo.eq(categoria.getCodigo()));
        predicate.and(StringUtils.isEmpty(categoria.getNome()) ? null : qCategoria.nome.containsIgnoreCase(categoria.getNome()));

        return predicate.getValue();
    }
}
