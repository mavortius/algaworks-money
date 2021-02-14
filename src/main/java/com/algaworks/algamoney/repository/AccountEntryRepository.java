package com.algaworks.algamoney.repository;

import com.algaworks.algamoney.model.AccountEntry;
import com.algaworks.algamoney.repository.querydsl.QuerydslRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AccountEntryRepository extends QuerydslRepository<AccountEntry, Long> {
  // List<AccountEntry> findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate data);
}
