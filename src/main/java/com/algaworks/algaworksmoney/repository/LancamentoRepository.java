package com.algaworks.algaworksmoney.repository;

import com.algaworks.algaworksmoney.model.Lancamento;
import com.algaworks.algaworksmoney.model.QLancamento;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface LancamentoRepository extends JpaRepository<Lancamento, Long>,
                                                QuerydslPredicateExecutor<Lancamento>,
                                                QuerydslBinderCustomizer<QLancamento> {
    @Override
    default void customize(QuerydslBindings bindings, QLancamento lancamento) {
        bindings.bind(String.class).first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
        bindings.bind(lancamento.dataVencimento).all(this::betweenDates);
        bindings.bind(lancamento.dataPagamento).all(this::betweenDates);
    }

    default Optional<Predicate> betweenDates(DatePath<LocalDate> path, Collection<? extends LocalDate> values) {
        List<LocalDate> dates = new ArrayList<>(values);

        if(dates.size() == 1) {
            return Optional.ofNullable(path.eq(dates.get(0)));
        } else {
            LocalDate from = dates.get(0);
            LocalDate to = dates.get(1);
            return Optional.of(path.between(from, to));
        }
    }

    List<Lancamento> findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate data);
}
